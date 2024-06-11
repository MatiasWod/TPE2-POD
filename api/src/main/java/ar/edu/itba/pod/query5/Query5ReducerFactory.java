package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.data.TopAgenciesFineAmount;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query5ReducerFactory implements ReducerFactory<String,TopAgenciesFineAmount,Double> {
    @Override
    public Reducer<TopAgenciesFineAmount,Double> newReducer(String s) {
        return new Reducer<>() {

            private double sumFineAmount;
            private int count;

            @Override
            public void beginReduce() {
                sumFineAmount = 0;
                count = 0;
            }

            @Override
            public void reduce(TopAgenciesFineAmount topAgenciesFineAmount) {
                sumFineAmount += topAgenciesFineAmount.getFineAmount();
                count++;
            }

            @Override
            public Double finalizeReduce() {
                return sumFineAmount / count;
            }
        };
    }
}
