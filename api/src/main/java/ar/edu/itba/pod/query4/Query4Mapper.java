package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.text.ParseException;
import java.util.Date;

public class Query4Mapper implements Mapper<String, Ticket, String, String> {
    private final Date dateTo;
    private final Date dateFrom;

    public Query4Mapper(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void map(String s, Ticket ticket, Context<String, String> context) {
        if (ticket.getCountyName() == null || ticket.getCountyName().isEmpty()) {
            return;
        }
        try {
            if (!(ticket.getIssueDate().after(dateFrom) && ticket.getIssueDate().before(dateTo))){
                return;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        context.emit(ticket.getCountyName(), ticket.getPlate());
    }
}

