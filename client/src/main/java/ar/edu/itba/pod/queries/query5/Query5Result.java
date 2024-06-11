package ar.edu.itba.pod.queries.query5;

import ar.edu.itba.pod.utils.Result;

public class Query5Result implements Result,Comparable<Query5Result> {
    private final String agencyName;
    private final double percentage;

    public Query5Result(String agencyName, double percentage) {
        this.agencyName = agencyName;
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return String.format("%s;%.2f%%",agencyName,percentage);
    }

    @Override
    public int compareTo(Query5Result o) {
        int res = Double.compare(o.percentage,this.percentage);
        if (res == 0){
            res = this.agencyName.compareTo(o.agencyName);
        }
        return res;
    }
}
