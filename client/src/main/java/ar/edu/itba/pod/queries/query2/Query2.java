package ar.edu.itba.pod.queries.query2;

import ar.edu.itba.pod.client.QueryClient;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.data.Top3Infractions;
import ar.edu.itba.pod.query2.Query2Mapper;
import ar.edu.itba.pod.query2.Query2ReducerFactory;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Query2 extends QueryClient {
    public Query2(){
        super();
    }

    @Override
    public void runQuery() throws ExecutionException, InterruptedException, IOException {
        final int topNumber= 3;

        final JobTracker jobTracker = getHazelcastInstance().getJobTracker("g10-namespace");
        final KeyValueSource<String, Ticket> keyValueSource = KeyValueSource.fromMultiMap(getHazelcastInstance().getMultiMap("g10-namespace-multimap"));
        Job<String, Ticket> job = jobTracker.newJob(keyValueSource);
        Map<String,Top3Infractions> reducedData = job
                .mapper(new Query2Mapper())
                .reducer(new Query2ReducerFactory())
                .submit()
                .get();
        Set<Query2Result> results = new TreeSet<>();
        for (Map.Entry<String,Top3Infractions> entry : reducedData.entrySet()) {
            results.add(new Query2Result(entry.getKey(), entry.getValue()));
        }
        writeResults(results);
    }

    @Override
    public String getQueryNumber() {
        return "2";
    }

    @Override
    public String getQueryHeader() {
        return "County;InfractionTop1;InfractionTop2;InfractionTop3";
    }

    public static void main(String[] args) {
        QueryClient query = new Query2();
    }
}
