package org.kt.share;

import org.kt.share.annotations.Log;

/*
    javap -c -verbose  TestLoggingImpl2.class > 1
*/

public class TestLoggingImpl2 implements TestLogging {

    @Log
    public void calculation(int param1) {
        System.out.println("logged param:" + param1);
    }

    public void calculation(int param1, int param2) {
        System.out.println("logged param:" + param1 + "," + param2);
    }

    @Log
    public void calculation(int param1, int param2, String param3) {
        System.out.println("Hello world!");
    }

    @Log
    public void method1() {}

    public void method1(int param1) {}

    @Log
    public void method1(String param1) {}

    public void method2(int param1, int param2) {}

    @Log
    public void method2(int param1, int param2, String param3) {
        System.out.println("logged param:" + param1 + "," + param2 + "," + param3);
    }

    @Log
    public void method3(String param1, int param2, int param3) {
        System.out.println("logged param:" + param1 + "," + param2 + "," + param3);
    }

    @Log
    public void method4(int param1, String param2, int param3) {
        System.out.println("logged param:" + param1 + "," + param2 + "," + param3);
    }

}

