package ar.edu.itba.pod.queries.query3;

import ar.edu.itba.pod.client.QueryClient;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.data.TopAgenciesFineAmount;
import ar.edu.itba.pod.query3.Query3Mapper;
import ar.edu.itba.pod.query3.Query3ReducerFactory;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Query3 extends QueryClient {

    private int numberOfResults;

    public Query3() {
        super();
    }

    @Override
    public void checkArguments() throws IllegalArgumentException {
        super.checkArguments();
        if (System.getProperty("n") == null) {
            throw new IllegalArgumentException("Argument 'n' must be provided");
        }
        try {
            numberOfResults = Integer.parseInt(System.getProperty("n"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Provided 'n' is not a number");
        }
    }

    @Override
    public void runQuery() throws ExecutionException, InterruptedException, IOException {
        final JobTracker jobTracker = getHazelcastInstance().getJobTracker("g10-namespace");
        final KeyValueSource<String,Ticket> keyValueSource = KeyValueSource.fromMultiMap(getHazelcastInstance().getMultiMap("g10-namespace-multimap"));
        Job<String,Ticket> job = jobTracker.newJob(keyValueSource);
        Map<String,Double> reducedData = job
                .mapper(new Query3Mapper())
                .reducer(new Query3ReducerFactory())
                .submit()
                .get();
        Set<Query3Result> results = new TreeSet<>();
        double totalFineAmount = reducedData.values().stream().mapToDouble(Double::doubleValue).sum();
        for (Map.Entry<String,Double> entry : reducedData.entrySet()){
            results.add(new Query3Result(entry.getKey(),(entry.getValue()/totalFineAmount) * 100));
        }
        writeResults(results.stream().limit(numberOfResults).collect(Collectors.toList()));
    }

    @Override
    public String getQueryNumber() {
        return "3";
    }

    @Override
    public String getQueryHeader() {
        return "Issuing Agency;Percentage";
    }

    public static void main(String[] args) {
        QueryClient query = new Query3();
    }
}
