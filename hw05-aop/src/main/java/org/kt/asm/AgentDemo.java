package org.kt.asm;

import org.kt.share.TestLogging;
import org.kt.share.TestLoggingImpl;

/*
    java -javaagent:AgentDemo.jar -jar AgentDemo.jar
*/
public class AgentDemo {
    public static void main(String[] args) {
        TestLogging testLogging = new TestLoggingImpl();
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
