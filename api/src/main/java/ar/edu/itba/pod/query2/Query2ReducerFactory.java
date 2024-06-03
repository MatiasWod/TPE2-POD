package ar.edu.itba.pod.query2;

import ar.edu.itba.pod.data.Top3Infractions;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;

public class Query2ReducerFactory implements ReducerFactory<String, String, Top3Infractions> {
    @Override
    public Reducer<String, Top3Infractions> newReducer(String string) {
        return new Reducer<>() {
            private Top3Infractions top3Infractions;
            private volatile Map<String, Integer> infractionsMap;

            @Override
            public void beginReduce() {
                top3Infractions = new Top3Infractions();
                infractionsMap = new HashMap<>();
            }

            @Override
            public void reduce(String value) {
                infractionsMap.put(value, infractionsMap.getOrDefault(value, 0) + 1);
                int count = infractionsMap.get(value);

                if (!top3Infractions.isInTop1(value) && count > top3Infractions.getAmountOfTicketsTop1()) {
                    top3Infractions.setTop3(top3Infractions.getTop2());
                    top3Infractions.setTop2(top3Infractions.getTop1());
                    top3Infractions.setTop1(value);
                } else if (!top3Infractions.isInTop2(value) && count > top3Infractions.getAmountOfTicketsTop2()) {
                    top3Infractions.setTop3(top3Infractions.getTop2());
                    top3Infractions.setTop2(value);
                } else if (!top3Infractions.isInTop3(value) && count > top3Infractions.getAmountOfTicketsTop3()) {
                    top3Infractions.setTop3(value);
                }

            }

            @Override
            public Top3Infractions finalizeReduce() {
                return top3Infractions;
            }

        };
    }
}
