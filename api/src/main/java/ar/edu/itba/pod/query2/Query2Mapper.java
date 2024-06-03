package ar.edu.itba.pod.query2;

import ar.edu.itba.pod.data.Infraction;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.data.Top3Infractions;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class Query2Mapper  implements Mapper<String, Ticket, String, String>, HazelcastInstanceAware {
    private transient Map<String, Infraction> infractionMap;


    @Override
    public void map(String string, Ticket ticket, Context<String, String> context) {
        if (!infractionMap.containsKey(ticket.getInfractionCode())) {
            return;
        }
        else if (ticket.getCountyName() == null || ticket.getCountyName().isEmpty()) {
            return;
        }
        context.emit(ticket.getCountyName(), infractionMap.get(ticket.getInfractionCode()).getDescription());
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.infractionMap = hazelcastInstance.getMap("g10-namespace-map");
    }
}
