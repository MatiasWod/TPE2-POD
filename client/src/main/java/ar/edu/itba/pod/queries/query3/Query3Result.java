package ar.edu.itba.pod.queries.query3;

import ar.edu.itba.pod.utils.Result;

public class Query3Result implements Result,Comparable<Query3Result> {
    private final String agencyName;
    private final double percentage;

    public Query3Result(String agencyName, double percentage) {
        this.agencyName = agencyName;
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return String.format("%s;%.2f%%",agencyName,percentage);
    }

    @Override
    public int compareTo(Query3Result o) {
        int res = Double.compare(o.percentage,this.percentage);
        if (res == 0){
            res = this.agencyName.compareTo(o.agencyName);
        }
        return res;
    }
}
