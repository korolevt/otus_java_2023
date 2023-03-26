package org.kt;

import org.kt.annotations.After;
import org.kt.annotations.Before;
import org.kt.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestDemo {

    public void runTest(Class<?> clazz, Method before, Method after, Method test) {
        try {
            Object object = clazz.getConstructor().newInstance();
            before.invoke(object);
            test.invoke(object);
            after.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);

        List<Method> tests = new ArrayList<>();
        Method methodBefore = null;
        Method methodAfter = null;

        for (Method method : clazz.getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof After) {
                    methodAfter = method;
                } else if (annotation instanceof Before) {
                    methodBefore = method;
                } else if (annotation instanceof Test) {
                    tests.add(method);
                }
            }
        }

        int countSuccess = 0;
        int countFailed = 0;

        for (Method methodTest : tests) {
            try {
                System.out.println(">>>>Starting " + methodTest.getName());
                runTest(clazz, methodBefore, methodAfter, methodTest);
                countSuccess++;
                System.out.println(">>>>Success " + methodTest.getName());
            } catch (Exception e) {
                countFailed++;
                System.out.println(">>>>Failed " + methodTest.getName());
            }
        }
        System.out.println("----------------");
        System.out.println("All tests: " + tests.size());
        System.out.println("Success tests: " + countSuccess);
        System.out.println("Failed tests: " + countFailed);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        new TestDemo().run("org.kt.SomeClass");
    }
}
