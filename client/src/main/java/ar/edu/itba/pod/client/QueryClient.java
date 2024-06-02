package ar.edu.itba.pod.client;

import ar.edu.itba.pod.data.Infraction;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.queries.Query1;
import ar.edu.itba.pod.utils.CityCSVDatasource;
import ar.edu.itba.pod.utils.ClientMethods;
import ar.edu.itba.pod.utils.Result;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public abstract class QueryClient {
    private static final Logger logger = LoggerFactory.getLogger(QueryClient.class);
    private static final String PROPERTY_ADDRESSES = "addresses";
    private static final String PROPERTY_CITY = "city";
    private static final String PROPERTY_INPATH = "inPath";
    private static final String PROPERTY_OUTPATH = "outPath";

    private HazelcastInstance hazelcastInstance;
    private String[] addresses;
    private String city;
    private Path outPath;
    private Path ticketsPath;
    private Path infractionsPath;

    public QueryClient() {
        int status = 0;
        try{
            checkArguments();
            this.hazelcastInstance = ClientMethods.clientConfiguration(this.addresses);
            loadData();
            runQuery();
        }catch (IllegalArgumentException e) {
            System.err.println("Oops! Invalid arguments were sent:\n" + e.getMessage());
            status = 64;
        }
        catch (ExecutionException | InterruptedException | IOException e){

        }
        finally {
            destroyData();
            if (this.hazelcastInstance != null){
                this.hazelcastInstance.shutdown();
            }
        }
        System.exit(status);
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    private void checkArguments() throws IllegalArgumentException{
        StringBuilder errors = new StringBuilder();

        String addressesArgument = System.getProperty(PROPERTY_ADDRESSES);
        String cityArgument = System.getProperty(PROPERTY_CITY);
        String inPathArgument =  System.getProperty(PROPERTY_INPATH);
        String outPathArgument = System.getProperty(PROPERTY_OUTPATH);

        if (addressesArgument == null) {
            errors.append("Argument 'addresses' must be provided\n");
        }
        if (cityArgument == null) {
            errors.append("Argument 'city' must be provided\n");
        }
        if (inPathArgument == null) {
            errors.append("Argument 'inPath' must be provided\n");
        }
        if (outPathArgument == null) {
            errors.append("Argument 'outPath' must be provided\n");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }

        this.addresses = addressesArgument.split(";");
        this.city = cityArgument;
        Path inPath = Path.of(inPathArgument);
        this.outPath = Path.of(outPathArgument);

        this.ticketsPath = Path.of(inPathArgument,"tickets" + cityArgument + ".csv");
        this.infractionsPath = Path.of(inPathArgument,"infractions" + cityArgument + ".csv");


    }

    public void writeResults(Collection<? extends Result> results) throws IOException {
        Path queryPath = outPath.resolve("query" + getQueryNumber() + ".csv");
        try (BufferedWriter writer = Files.newBufferedWriter(
                queryPath, StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write(getQueryHeader());
            writer.newLine();
            for (Result result : results) {
                writer.write(result.toString());
                writer.newLine();
            }
        }
    }

    private void destroyData() {
        if (hazelcastInstance == null) {
            return;
        }

        hazelcastInstance.getMap("g10-namespace-map").clear();
        hazelcastInstance.getMultiMap("g10-namespace-multimap").clear();
    }

    private void loadData(){
        if (hazelcastInstance == null){
            return;
        }

        try(
                ExecutorService executorService = Executors.newCachedThreadPool()
                ){
            executorService.submit(new LoadTicketsRunnable(hazelcastInstance.getMultiMap("g10-namespace-multimap"),city,ticketsPath));
            executorService.submit(new LoadInfractionsRunnable(hazelcastInstance.getMap("g10-namespace-map"),city,infractionsPath));
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e){
            System.exit(1);
        }

    }

    private static class LoadTicketsRunnable implements Runnable{
        private final MultiMap<String, Ticket> multiMap;
        private final String city;
        private final Path path;

        public LoadTicketsRunnable(MultiMap<String, Ticket> multiMap, String city, Path path) {
            this.multiMap = multiMap;
            this.city = city;
            this.path = path;
        }

        @Override
        public void run() {
            try(
                    Stream<String> lines = Files.lines(path).skip(1).parallel()
            )
            {
                CityCSVDatasource cityCSVDatasource = CityCSVDatasource.valueOf(city);
                lines.forEach(line -> {
                    String[] fields = line.split(";");
                    multiMap.put(
                            fields[2],
                            cityCSVDatasource.ticketFromCSV(fields)
                    );
                });
            }
            catch (IOException e){

            }
        }
    }

    private static class LoadInfractionsRunnable implements Runnable{
        private final Map<String, Infraction> map;
        private final String city;
        private final Path path;

        public LoadInfractionsRunnable(Map<String, Infraction> map, String city, Path path) {
            this.map = map;
            this.city = city;
            this.path = path;
        }

        @Override
        public void run() {
            try(
                    Stream<String> lines = Files.lines(path).skip(1).parallel()
            )
            {
                CityCSVDatasource cityCSVDatasource = CityCSVDatasource.valueOf(city);
                lines.forEach(line -> {
                    String[] fields = line.split(";");

                    map.put(
                            fields[0],
                            cityCSVDatasource.infractionFromCSV(fields)
                    );
                });
            }
            catch (IOException e){

            }
        }
    }

    public abstract void runQuery() throws ExecutionException, InterruptedException, IOException;
    public abstract String getQueryNumber();
    public abstract String getQueryHeader();
}
