package org.kt;

import org.kt.annotations.After;
import org.kt.annotations.Before;
import org.kt.annotations.Test;

public class SomeClass {

    @Before
    void Before() {
        System.out.println("--Before");
    }

    @After
    void After() {
        System.out.println("--After");
    }

    @Test
    void SomeMethod() {
        System.out.println("--SomeMethod 1");
    }

    @Test
    void SomeMethod2() {
        throw new NullPointerException();
        //System.out.println("--SomeMethod 2");
    }

    @Test
    void SomeMethod3() {
        System.out.println("--SomeMethod 3");
    }

    void SomeMethod4() {
        System.out.println("--SomeMethod 4");
    }
}
