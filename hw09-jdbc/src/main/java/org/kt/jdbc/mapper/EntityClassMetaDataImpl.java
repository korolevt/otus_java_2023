package org.kt.jdbc.mapper;

import org.kt.crm.annotations.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final String name;
    private final List<Field> fields = new ArrayList<>();
    private final List<Field> fieldsWithoutId = new ArrayList<>();
    private Field idField;
    private Constructor<?> constructor;

    public EntityClassMetaDataImpl() {
        var clazz = getGenericParameterClass();
        name = clazz.getSimpleName();
        // Разбор полей и поиск аннотации @Id
        for (Field field : clazz.getDeclaredFields()) {
            boolean isId = false;
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Id) {
                    idField = field;
                    isId = true;
                    break;
                }
            }
            if (!isId)
                fieldsWithoutId.add(field);
            fields.add(field);
        }
        // Поиск конструктора без параметров
        var ctors  = clazz.getConstructors();
        for (var ctor : ctors) {
            if (ctor.getGenericParameterTypes().length == 0) {
                this.constructor = ctor;
                break;
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Constructor<T> getConstructor() {
        return (Constructor<T>)this.constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private Class<?> getGenericParameterClass() {
        return (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
