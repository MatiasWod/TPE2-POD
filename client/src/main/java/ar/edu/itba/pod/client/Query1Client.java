package ar.edu.itba.pod.client;

import ar.edu.itba.pod.data.Infraction;
import ar.edu.itba.pod.queries.Query1;
import ar.edu.itba.pod.utils.CityCSVDatasource;
import ar.edu.itba.pod.utils.ClientMethods;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Query1Client {
    private static final Logger logger = LoggerFactory.getLogger(Query1Client.class);
    private static final int QUERY_NUMBER = 1;
    private static final String PROPERTY_ADDRESSES = "addresses";
    private static final String PROPERTY_CITY = "city";
    private static final String PROPERTY_INPATH = "inPath";
    private static final String PROPERTY_OUTPATH = "outPath";

    public static void main(String[] args) {
        logger.info("hz-config Query1Client Starting ...");

        final String addresses = System.getProperty(PROPERTY_ADDRESSES);
        final String city = System.getProperty(PROPERTY_CITY);
        final String inPath =  System.getProperty(PROPERTY_INPATH);
        final String outPath = System.getProperty(PROPERTY_OUTPATH);

        HazelcastInstance hazelcastInstance = ClientMethods.clientConfiguration(addresses.split(";"));

        IList<Infraction> infractionIList = hazelcastInstance.getList("infractions");

        CityCSVDatasource datasource = CityCSVDatasource.valueOf(city);

        try{
            List<String> stream = Files.readAllLines(Paths.get(inPath + "infractions" + city + ".csv"), StandardCharsets.ISO_8859_1);
            List<Infraction> infractionList = stream.stream().skip(1).map(line -> line.split(";")).map(datasource::infractionFromCSV).toList();
            infractionIList.addAll(infractionList);
        }
        catch (IOException e){
            return;
        }

        Query1 query1 = new Query1(infractionIList,hazelcastInstance,outPath + "query1.csv");

        try {
            query1.run();
        }
        catch (ExecutionException | InterruptedException | IOException e){
            return;
        }

        // Shutdown
        infractionIList.clear();
        HazelcastClient.shutdownAll();
    }
}
