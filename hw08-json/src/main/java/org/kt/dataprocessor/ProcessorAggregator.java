package org.kt.dataprocessor;

import org.kt.model.Measurement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        //сразу указывается тип мапы, которая себя сортирует
        return data.stream()
                .collect(groupingBy(Measurement::getName, LinkedHashMap::new,
                        summingDouble(Measurement::getValue)));
    }

/*
    @Override
    public Map<String, Double> process(List<Measurement> data) {
            //группирует выходящий список по name, при этом суммирует поля value
            //далее сортировка по name
        return data.stream()
            .collect(Collectors.collectingAndThen(
                groupingBy(Measurement::getName,
                    summingDouble(Measurement::getValue)),
                m -> m.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new))));

        }
    }*/
}
