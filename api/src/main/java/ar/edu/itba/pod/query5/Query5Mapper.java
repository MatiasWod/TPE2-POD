package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.data.Infraction;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.data.TopAgenciesFineAmount;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class Query5Mapper implements Mapper<String,Ticket,String,TopAgenciesFineAmount>, HazelcastInstanceAware {
    private transient Map<String, Infraction> infractionMap;

    public Query5Mapper() {
    }

    @Override
    public void map(String s, Ticket ticket, Context<String, TopAgenciesFineAmount> context) {
        if (!infractionMap.containsKey(ticket.getInfractionCode())) {
            return;
        }
        Infraction infraction = infractionMap.get(ticket.getInfractionCode());
        context.emit(infraction.getDescription(),new TopAgenciesFineAmount(infraction.getDescription(), ticket.getFineAmount()));
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.infractionMap = hazelcastInstance.getMap("g10-namespace-map");
    }
}
