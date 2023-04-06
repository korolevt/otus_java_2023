package org.kt.share;

import org.kt.share.annotations.Log;

public class TestLogging implements TestLoggingInterface {

    @Log
    public void calculation(int param1) {
        System.out.println("Hello world!");
    }

    public void calculation(int param1, int param2) {}

    @Log
    public void calculation(int param1, int param2, String param3) {}

    @Log
    public void method1() {}

    public void method1(int param1) {}

    @Log
    public void method1(String param1) {}

    public void method2(int param1, int param2) {}

    @Log
    public void method2(int param1, int param2, String param3) {}
}
