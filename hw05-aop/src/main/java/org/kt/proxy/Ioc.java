package org.kt.proxy;

import org.kt.share.TestLogging;
import org.kt.share.TestLoggingInterface;
import org.kt.share.annotations.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class Ioc {

    private Ioc() {
    }

    static TestLoggingInterface createTestLogging() {
        InvocationHandler handler = new TestLoggingInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class TestLoggingInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        final private Map<String, Method> methodLogs = new HashMap<>();
        private String getMethodSignature(Method method) {
            return method.getName() + Arrays.toString(method.getParameterTypes());
        }
        TestLoggingInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
            for (Method method : myClass.getClass().getDeclaredMethods()) {
                Annotation[] annotations = method.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Log) {
                        methodLogs.put(getMethodSignature(method), method);
                    }
                }
            }
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodLogs.containsKey(getMethodSignature(method))) {
                System.out.println("execution method: " + method.getName() + ", params: " + (args != null ? Arrays.toString(args) : ""));
            }

            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "TestLoggingInvocationHandler{" +
                    "testLoggingClass=" + myClass +
                    '}';
        }
    }
}
