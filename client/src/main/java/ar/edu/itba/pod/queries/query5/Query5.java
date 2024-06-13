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
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Query5 extends QueryClient {
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

        // Me fijo a que grupo pertenece cada infraccion
        Map<Integer, List<String>> infractionsByGroup = getInfractionsByGroup(reducedData);

        Set<Query5Result> results = formatMapIntoQueryResults(infractionsByGroup);

        writeResults(results);
    }

    private Map<Integer, List<String>> getInfractionsByGroup(Map<String, Double> data) {
        Map<Integer, List<String>> map = new HashMap<>();

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            Integer groupValue = entry.getValue().intValue() / 100;

            map.putIfAbsent(groupValue, new ArrayList<>());
            map.get(groupValue).add(entry.getKey());
        }
        return map;
    }

    private Set<Query5Result> formatMapIntoQueryResults(Map<Integer, List<String>> infractionsByGroup) {
        Set<Query5Result> results = new TreeSet<>();

        for (Map.Entry<Integer, List<String>> entry : infractionsByGroup.entrySet()) {
            List<String> list = entry.getValue();

            // If belongs to group 0-100
            if (entry.getKey() == 0) continue;

            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j <  list.size(); j++) {
                    // If infraction A != Infraction B
                    if (!list.get(i).equals(list.get(j))) {
                        results.add(new Query5Result(entry.getKey() * 100, list.get(i), list.get(j)));
                    }
                }
            }
        }
        return results;
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
