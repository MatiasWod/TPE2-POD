package ar.edu.itba.pod.queries.query5;

import ar.edu.itba.pod.utils.Result;

import java.util.Objects;

public class Query5Result implements Result, Comparable<Query5Result> {
    private final Integer group;
    private final String infractionA;
    private final String infractionB;

    public Query5Result(Integer group, String infractionA, String infractionB) {
        this.group = group;
        this.infractionA = infractionA;
        this.infractionB = infractionB;
    }

    @Override
    public String toString() {
        return String.format("%d;%s;%s", group, infractionA, infractionB);
    }

    @Override
    public int compareTo(Query5Result o) {
        int groupComparison = this.group.compareTo(o.group);
        if (groupComparison != 0) {
            return groupComparison;
        }

        // Create sorted pairs to ensure (A, B) is treated the same as (B, A)
        String thisInfractionPair = this.infractionA.compareTo(this.infractionB) <= 0
                ? this.infractionA + this.infractionB
                : this.infractionB + this.infractionA;

        String otherInfractionPair = o.infractionA.compareTo(o.infractionB) <= 0
                ? o.infractionA + o.infractionB
                : o.infractionB + o.infractionA;

        return thisInfractionPair.compareTo(otherInfractionPair);
    }
}
