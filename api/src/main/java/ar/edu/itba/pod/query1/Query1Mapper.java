package ar.edu.itba.pod.query1;

import ar.edu.itba.pod.data.Infraction;
import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class Query1Mapper implements Mapper<String, Infraction, String, Integer> {

    @Override
    public void map(String s, Infraction infraction, Context<String, Integer> context) {
        context.emit(infraction.getDescription().toUpperCase(), 1);
    }
}
