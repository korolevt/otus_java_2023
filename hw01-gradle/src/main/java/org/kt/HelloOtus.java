package org.kt;

import com.google.common.base.Joiner;

public class HelloOtus {
    public static void main(String[] args) {
        Joiner joiner = Joiner.on("_").skipNulls();
        String str = joiner.join("Otus", null, "Java", "2023");

        System.out.println(str);
    }
}
