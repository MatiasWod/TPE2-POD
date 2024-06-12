package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.data.PlateInfractions;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;

public class Query4ReducerFactory implements ReducerFactory<String, String, PlateInfractions> {
    @Override
    public Reducer<String, PlateInfractions> newReducer(String s) {
        return new Reducer<>() {

            private volatile Map<String, Integer> PlateMap;
            private volatile PlateInfractions maxInfractionsPlate = new PlateInfractions(null, -1);

            @Override
            public void beginReduce() {
                PlateMap = new HashMap<>();
            }

            @Override
            public void reduce(String value){
                int currentInfractionCount = PlateMap.getOrDefault(value, 0) + 1;
                PlateMap.put(value, currentInfractionCount);

                if(currentInfractionCount > maxInfractionsPlate.getInfractionsAmount()){
                    maxInfractionsPlate.setInfractionsAmount(currentInfractionCount);
                    maxInfractionsPlate.setPlate(value);
                }
            }

            @Override
            public PlateInfractions finalizeReduce(){
                return maxInfractionsPlate;
            }
        };
    }

}
