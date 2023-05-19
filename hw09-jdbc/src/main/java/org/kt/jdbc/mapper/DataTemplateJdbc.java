package org.kt.jdbc.mapper;

import org.kt.core.repository.DataTemplate;
import org.kt.core.repository.DataTemplateException;
import org.kt.core.repository.executor.DbExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createObject(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var clientList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    clientList.add(createObject(rs));
                }
                return clientList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                    getParameters(object));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                    List.of(getParameters(object), getId(object)));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createObject(ResultSet rs) {
        try {
            var object = entityClassMetaData.getConstructor().newInstance();
            for (int idx = 0; idx < rs.getMetaData().getColumnCount(); idx++) {
                String name = rs.getMetaData().getColumnName(idx + 1);
                Object value = rs.getObject(idx + 1);
                var field = entityClassMetaData.getAllFields().stream()
                        .filter(f -> f.getName().equals(name)).findFirst();
                if (field.isPresent()) {
                    field.get().setAccessible(true);
                    field.get().set(object, value);
                }
            }
            return object;
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getParameters(T object) {
        try {
            List<Object> list = new ArrayList<>();
            for (var field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                list.add(field.get(object));
            }
            return list;
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private Object getId(T object) {
        try {
            var fieldId = entityClassMetaData.getIdField();
            fieldId.setAccessible(true);
            return fieldId.get(object);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

}
