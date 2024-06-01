package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.data.Infraction;
import ar.edu.itba.pod.data.Pair;
import ar.edu.itba.pod.query1.Query1Collator;
import ar.edu.itba.pod.query1.Query1CombinerFactory;
import ar.edu.itba.pod.query1.Query1Mapper;
import ar.edu.itba.pod.query1.Query1ReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Query1 implements Query{

    private IList<Infraction> infractionIList;
    private final HazelcastInstance hazelcastInstance;
    private List<Pair<String,Integer>> result;
    private String resultPath;

    public Query1(IList<Infraction> infractionIList, HazelcastInstance hazelcastInstance, String resultPath) {
        this.infractionIList = infractionIList;
        this.hazelcastInstance = hazelcastInstance;
        this.resultPath = resultPath;
    }

    @Override
    public void run() throws ExecutionException, InterruptedException, IOException {
        final JobTracker jobTracker = hazelcastInstance.getJobTracker("g10_query1");
        final KeyValueSource<String,Infraction> keyValueSource = KeyValueSource.fromList(infractionIList);
        final Job<String,Infraction> job = jobTracker.newJob(keyValueSource);
        final ICompletableFuture<List<Pair<String,Integer>>> completableFuture = job
                .mapper(new Query1Mapper())
                .combiner(new Query1CombinerFactory())
                .reducer(new Query1ReducerFactory())
                .submit(new Query1Collator());
        result = completableFuture.get();
        write(resultPath);
    }

    @Override
    public void write(String path) throws IOException {
        List<String> resLines = new ArrayList<>();
        resLines.add("Infractions;Tickets");
        result.forEach(res -> resLines.add(res.toString()));
        Path outPath = Paths.get(path);
        Files.write(outPath,resLines);
    }
}
