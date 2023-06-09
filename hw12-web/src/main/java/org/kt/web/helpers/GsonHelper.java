package org.kt.web.helpers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonHelper {
    public static Gson createGson() {
        ExclusionStrategy strategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }

            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                return field.getAnnotation(ExcludeGson.class) != null;
            }
        };

        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .addSerializationExclusionStrategy(strategy)
                .create();
    }
}
