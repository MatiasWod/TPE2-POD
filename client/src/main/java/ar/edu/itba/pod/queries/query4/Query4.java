package ar.edu.itba.pod.queries.query4;

import ar.edu.itba.pod.client.QueryClient;
import ar.edu.itba.pod.data.PlateInfractions;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.queries.query4.Query4Result;
import ar.edu.itba.pod.queries.query4.Query4Result;
import ar.edu.itba.pod.query4.Query4Mapper;
import ar.edu.itba.pod.query4.Query4ReducerFactory;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;


import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class Query4 extends QueryClient {
    public Query4(){
        super();
    }

    private Date dateFrom;
    private Date dateTo;


    @Override
    public void checkArguments() throws IllegalArgumentException {
        super.checkArguments();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        if (System.getProperty("from") == null) {
            throw new IllegalArgumentException("Argument 'from' must be provided");
        }
        if (System.getProperty("to") == null) {
            throw new IllegalArgumentException("Argument 'to' must be provided");
        }
        try {
            dateFrom = sdf.parse(System.getProperty("from"));
            dateTo = sdf.parse(System.getProperty("to"));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Provided 'from' and/or 'to' are not dates in 'dd/MM/yyyy' format");
        }
    }

    @Override
    public void runQuery() throws ExecutionException, InterruptedException, IOException {
        final JobTracker jobTracker = getHazelcastInstance().getJobTracker("g10-namespace");
        final KeyValueSource<String, Ticket> keyValueSource = KeyValueSource.fromMultiMap(getHazelcastInstance().getMultiMap("g10-namespace-multimap"));
        Job<String,Ticket> job = jobTracker.newJob(keyValueSource);
        Map<String, PlateInfractions> reducedData = job
                .mapper(new Query4Mapper(dateFrom, dateTo))
                .reducer(new Query4ReducerFactory())
                .submit()
                .get();
        Set<Query4Result> results = new TreeSet<>();
        for (Map.Entry<String,PlateInfractions> entry : reducedData.entrySet()) {
            results.add(new Query4Result(entry.getKey(), entry.getValue().getPlate(), entry.getValue().getInfractionsAmount()));
        }
        writeResults(results);
    }

    @Override
    public String getQueryNumber() {
        return "4";
    }

    @Override
    public String getQueryHeader() {
        return "County;Plate;Tickets";
    }

    public static void main(String[] args) {
        QueryClient query = new ar.edu.itba.pod.queries.query4.Query4();
    }
}
