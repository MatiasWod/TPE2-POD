package ar.edu.itba.pod.query3;

import ar.edu.itba.pod.data.TopAgenciesFineAmount;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;

public class Query3ReducerFactory implements ReducerFactory<String,TopAgenciesFineAmount,Double> {
    @Override
    public Reducer<TopAgenciesFineAmount,Double> newReducer(String s) {
        return new Reducer<>() {

            private double sumFineAmount;

            @Override
            public void beginReduce() {
                sumFineAmount = 0;
            }

            @Override
            public void reduce(TopAgenciesFineAmount topAgenciesFineAmount) {
                sumFineAmount += topAgenciesFineAmount.getFineAmount();
            }

            @Override
            public Double finalizeReduce() {
                return sumFineAmount;
            }
        };
    }
}
