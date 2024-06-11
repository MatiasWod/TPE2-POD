package ar.edu.itba.pod.queries.query5;

import ar.edu.itba.pod.client.QueryClient;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.query3.Query3Mapper;
import ar.edu.itba.pod.query3.Query3ReducerFactory;
import ar.edu.itba.pod.query5.Query5Mapper;
import ar.edu.itba.pod.query5.Query5ReducerFactory;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Query5 extends QueryClient {

    private int numberOfResults;

    public Query5() {
        super();
    }

    @Override
    public void runQuery() throws ExecutionException, InterruptedException, IOException {
        final JobTracker jobTracker = getHazelcastInstance().getJobTracker("g10-namespace");
        final KeyValueSource<String,Ticket> keyValueSource = KeyValueSource.fromMultiMap(getHazelcastInstance().getMultiMap("g10-namespace-multimap"));
        Job<String,Ticket> job = jobTracker.newJob(keyValueSource);
        Map<String,Double> reducedData = job
                .mapper(new Query5Mapper())
                .reducer(new Query5ReducerFactory())
                .submit()
                .get();
        Set<Query5Result> results = new TreeSet<>();
        double totalFineAmount = reducedData.values().stream().reduce(0.0,Double::sum);
        for (Map.Entry<String,Double> entry : reducedData.entrySet()){
            results.add(new Query5Result(entry.getKey(),(Math.floor((entry.getValue()/totalFineAmount) * 10000) )/100));
        }
        writeResults(results.stream().limit(numberOfResults).collect(Collectors.toList()));
    }

    @Override
    public String getQueryNumber() {
        return "5";
    }

    @Override
    public String getQueryHeader() {
        return "Group;Infraction A;Infraction B";
    }

    public static void main(String[] args) {
        QueryClient query = new Query5();
    }
}
