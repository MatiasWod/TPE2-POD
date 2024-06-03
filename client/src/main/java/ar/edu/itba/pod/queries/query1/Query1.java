package ar.edu.itba.pod.queries.query1;

import ar.edu.itba.pod.client.QueryClient;
import ar.edu.itba.pod.data.Infraction;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.query1.Query1Mapper;
import ar.edu.itba.pod.query1.Query1ReducerFactory;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Query1 extends QueryClient {

    public Query1() {
        super();
    }

    @Override
    public void runQuery() throws ExecutionException, InterruptedException, IOException {
        final JobTracker jobTracker = getHazelcastInstance().getJobTracker("g10-namespace");
        final KeyValueSource<String, Ticket> keyValueSource = KeyValueSource.fromMultiMap(getHazelcastInstance().getMultiMap("g10-namespace-multimap"));
        Job<String,Ticket> job = jobTracker.newJob(keyValueSource);
        Map<String,Integer> reducedData = job
                .mapper(new Query1Mapper())
                .reducer(new Query1ReducerFactory())
                .submit()
                .get();
        Map<String,Infraction> infractionMap = getHazelcastInstance().getMap("g10-namespace-map");
        MultiMap<String,Ticket> ticketMultiMap = getHazelcastInstance().getMultiMap("g10-namespace-multimap");
        Set<Query1Result> results = new TreeSet<>();
        for (Map.Entry<String,Integer> entry : reducedData.entrySet()) {
            results.add(new Query1Result(entry.getKey(), entry.getValue()));
        }
        writeResults(results);
    }

    @Override
    public String getQueryNumber() {
        return "1";
    }

    @Override
    public String getQueryHeader() {
        return "Infractions;Tickets";
    }

    public static void main(String[] args) {
        QueryClient query = new Query1();
    }
}