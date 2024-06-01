package ar.edu.itba.pod.query1;

import ar.edu.itba.pod.data.Pair;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Query1Collator implements Collator<Map.Entry<String,Integer>, List<Pair<String,Integer>>> {
    @Override
    public List<Pair<String, Integer>> collate(Iterable<Map.Entry<String, Integer>> iterable) {
        List<Pair<String,Integer>> pairList = new ArrayList<>();
        Iterator<Map.Entry<String,Integer>> iterator = iterable.iterator();
        Map.Entry<String,Integer> entry;

        while (iterator.hasNext()){
            entry = iterator.next();
            pairList.add(new Pair<>(entry.getKey(), entry.getValue()));
        }

        pairList.sort( (p1,p2) -> {
            int res = p2.getRight().compareTo(p1.getRight());
            if (res != 0 ){ return res;}
            else{
                return p2.getLeft().compareTo(p1.getLeft());
            }
        });

        return pairList;
    }
}
