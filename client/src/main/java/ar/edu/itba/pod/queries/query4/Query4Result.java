package ar.edu.itba.pod.queries.query4;

import ar.edu.itba.pod.utils.Result;

public class Query4Result implements Result, Comparable<Query4Result> {
    private final String county;
    private final String plate;
    private final int numberOfInfractions;

    public Query4Result(String county, String plate, int numberOfInfractions) {
        this.county = county;
        this.plate = plate;
        this.numberOfInfractions = numberOfInfractions;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%d", county, plate, numberOfInfractions);
    }

    @Override
    public int compareTo(Query4Result o) {
        return this.county.compareTo(o.county);
    }
}
