package org.kt.jdbc.mapper;

import org.kt.crm.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<?> clazz;

    public EntityClassMetaDataImpl() {
        clazz = getGenericParameterClass();
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Constructor<T> getConstructor() {
        try {
            return (Constructor<T>)clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Not found default constructor");
        }
    }

    @Override
    public Field getIdField() {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new RuntimeException("Not found @Id annotation");
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(clazz.getDeclaredFields()).toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> result = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class)) {
                result.add(field);
            }
        }
        return result;
    }

    private Class<?> getGenericParameterClass() {
        return (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
