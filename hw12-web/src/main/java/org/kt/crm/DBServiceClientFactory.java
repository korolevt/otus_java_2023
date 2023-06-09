package org.kt.crm;

import org.hibernate.cfg.Configuration;
import org.kt.core.repository.DataTemplateHibernate;
import org.kt.core.repository.HibernateUtils;
import org.kt.core.sessionmanager.TransactionManagerHibernate;
import org.kt.crm.dbmigrations.MigrationsExecutorFlyway;
import org.kt.crm.model.Address;
import org.kt.crm.model.Client;
import org.kt.crm.model.Phone;
import org.kt.crm.service.DBServiceClient;
import org.kt.crm.service.DbServiceClientImpl;

public final class DBServiceClientFactory {
    public static DBServiceClient getDbServiceClient(String hibertateConfigFile) {
        var configuration = new Configuration().configure(hibertateConfigFile);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }
}
