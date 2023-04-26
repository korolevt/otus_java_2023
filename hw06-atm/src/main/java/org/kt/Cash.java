package org.kt;

import org.kt.interfaces.Copyable;

import java.util.Map;
import java.util.TreeMap;

public class Cash implements Copyable<Cash> {
    private final TreeMap<BanknoteType, Integer> map =
            new TreeMap<>((o1, o2) -> o2.getValue() - o1.getValue());

    public Map.Entry<BanknoteType, Integer> getFirst() {
        return map.firstEntry();
    }

    public Map.Entry<BanknoteType, Integer> getNext(BanknoteType banknote) {
        return map.higherEntry(banknote);
    }

    public void put(BanknoteType banknote, Integer count) {
        map.put(banknote, count);
    }

    @Override
    public Cash copy() {
        Cash copy = new Cash();
        for (var e: map.entrySet()) {
            copy.put(e.getKey(), e.getValue());
        }
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        var e = getFirst();
        while (e != null) {
            BanknoteType banknote = e.getKey();
            int count = e.getValue();
            stringBuilder.append(banknote + ": " + count + " шт.");
            e = getNext(banknote);
            if (e != null) stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
