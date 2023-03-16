package org.kt;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    TreeMap<Customer, String> map =
        new TreeMap<>((o1, o2) -> (int) (o1.getScores() - o2.getScores()));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> me = map.firstEntry();
        return Map.entry(me.getKey().newInstance(), me.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> me = map.higherEntry(customer);
        if (me == null) return null;

        return Map.entry(me.getKey().newInstance(), me.getValue());
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
