package ar.edu.itba.pod.query1;

import ar.edu.itba.pod.data.Infraction;
import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class Query1Mapper implements Mapper<Integer, Ticket, String, Integer>,HazelcastInstanceAware {

    private transient Map<Integer,Infraction> infractionMap;

    public Query1Mapper() {
    }

    @Override
    public void map(Integer n, Ticket ticket, Context<String, Integer> context) {
        if(!infractionMap.containsKey(ticket.getInfractionCode())){
            return;
        }
        context.emit(infractionMap.get(ticket.getInfractionCode()).getDescription(), 1);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.infractionMap = hazelcastInstance.getMap("g10-namespace-map");
    }
}
