package org.kt.proxy;

import org.kt.share.TestLogging;
import org.kt.share.TestLoggingImpl;
import org.kt.share.annotations.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class Ioc {

    private Ioc() {
    }

    static TestLogging createTestLogging() {
        InvocationHandler handler = new Handler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class Handler implements InvocationHandler {
        private final Object origin;
        final private Map<String, Method> methodLogs = new HashMap<>();
        private String getMethodSignature(Method method) {
            return method.getName() + Arrays.toString(method.getParameterTypes());
        }
        Handler(Object origin) {
            this.origin = origin;
            for (Method method : origin.getClass().getDeclaredMethods()) {
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

            return method.invoke(origin, args);
        }

        @Override
        public String toString() {
            return "Handler{" +
                    "myClass=" + origin +
                    '}';
        }
    }
}
