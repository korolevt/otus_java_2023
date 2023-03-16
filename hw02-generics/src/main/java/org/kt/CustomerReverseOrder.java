package org.kt;

import java.util.*;

public class CustomerReverseOrder {
    Deque<Customer> deque = new LinkedList<>();

    public void add(Customer customer) {
        deque.add(customer);
    }

    public Customer take() {
        return deque.removeLast();
    }
}
