package org.kt;

import java.util.*;

public class CustomerReverseOrder {
    LinkedList<Customer> list = new LinkedList<>();

    public void add(Customer customer) {
        list.add(customer);
    }

    public Customer take() {
        return list.removeLast();
    }
}
