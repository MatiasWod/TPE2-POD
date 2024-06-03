package ar.edu.itba.pod.queries.query1;

import ar.edu.itba.pod.utils.Result;

public class Query1Result implements Result,Comparable<Query1Result> {
    private final String infractionDescription;
    private final int amountOfTickets;

    public Query1Result(String infractionDescription, int amountOfTickets) {
        this.infractionDescription = infractionDescription;
        this.amountOfTickets = amountOfTickets;
    }

    @Override
    public String toString() {
        return String.format("%s;%d",infractionDescription,amountOfTickets);
    }

    @Override
    public int compareTo(Query1Result o) {
        int res = Integer.compare(o.amountOfTickets, this.amountOfTickets);
        if (res == 0) {
            res = this.infractionDescription.compareTo(o.infractionDescription);
        }
        return res;
    }
}
