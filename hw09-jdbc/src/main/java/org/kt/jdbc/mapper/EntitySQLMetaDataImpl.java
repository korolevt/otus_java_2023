package org.kt.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private static final Logger log = LoggerFactory.getLogger(EntitySQLMetaDataImpl.class);

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        String params = this.entityClassMetaData.getAllFields().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        String sql = String.format("select %s from %s", params,
                entityClassMetaData.getName()).toLowerCase();
        log.debug("selectAllSql: {}", sql);
        return sql;
    }

    @Override
    public String getSelectByIdSql() {
        String sql = String.format("%s where %s = ?",
                getSelectAllSql(),
                entityClassMetaData.getIdField().getName()).toLowerCase();
        log.debug("selectByIdSql: {}", sql);
        return sql;
    }

    @Override
    public String getInsertSql() {
        String params = this.entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        String questions = this.entityClassMetaData.getFieldsWithoutId().stream()
                .map(x -> "?")
                .collect(Collectors.joining(", "));

        String sql = String.format("insert into %s(%s) values (%s)",
                entityClassMetaData.getName(),
                params,
                questions).toLowerCase();
        log.debug("insertSql: {}", sql);
        return sql;
    }

    @Override
    public String getUpdateSql() {
        String parameters = this.entityClassMetaData.getFieldsWithoutId().stream()
                .map(x -> x.getName() + " = ?")
                .collect(Collectors.joining(", "));

        String sql = String.format("update %s set %s where id = ?",
                entityClassMetaData.getName(),
                parameters).toLowerCase();
        log.debug("updateSql: {}", sql);
        return sql;
    }
}
