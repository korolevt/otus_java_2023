package org.kt.proxy;

import org.kt.share.TestLogging;

public class ProxyDemo {
    public static void main(String[] args) {
        TestLogging testLogging = Ioc.createTestLogging();
        testLogging.calculation(1);
        testLogging.calculation(2, 3);
        testLogging.calculation(4, 5, "6");
        testLogging.method1(); // logging without parameters
        testLogging.method1(7);
        testLogging.method1("8");
        testLogging.method2(9, 10);
        testLogging.method2(11, 12, "13");
    }
}
