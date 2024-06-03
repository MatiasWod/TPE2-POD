package ar.edu.itba.pod.queries.query2;

import ar.edu.itba.pod.data.Top3Infractions;
import ar.edu.itba.pod.utils.Result;

public class Query2Result implements Result, Comparable<Query2Result> {
    private final String County;
    private final Top3Infractions top3Infractions;

    public Query2Result(String County, Top3Infractions top3Infractions) {
        this.County = County;
        this.top3Infractions = top3Infractions;
    }

    @Override
    public String toString() {
        if (top3Infractions.getTop1() == null) {
            top3Infractions.setTop1("-");
        }
        if (top3Infractions.getTop2() == null) {
            top3Infractions.setTop2("-");
        }
        if (top3Infractions.getTop3() == null) {
            top3Infractions.setTop3("-");
        }
        return String.format("%s;%s;%s;%s", County, top3Infractions.getTop1(), top3Infractions.getTop2(), top3Infractions.getTop3());
    }

    @Override
    public int compareTo(Query2Result o) {
        return this.County.compareTo(o.County);
    }
}