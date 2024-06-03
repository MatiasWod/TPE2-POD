package ar.edu.itba.pod.query3;

import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.data.TopAgenciesFineAmount;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query3Mapper implements Mapper<String,Ticket,String,TopAgenciesFineAmount> {
    public Query3Mapper() {
    }

    @Override
    public void map(String s, Ticket ticket, Context<String, TopAgenciesFineAmount> context) {
        context.emit(ticket.getAgency(),new TopAgenciesFineAmount(ticket.getAgency(), ticket.getFineAmount()));
    }
}
