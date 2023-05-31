package org.kt.demo;

import org.hibernate.cfg.Configuration;
import org.kt.cache.MyCache;
import org.kt.core.repository.DataTemplateHibernate;
import org.kt.core.repository.HibernateUtils;
import org.kt.core.sessionmanager.TransactionManagerHibernate;
import org.kt.crm.dbmigrations.MigrationsExecutorFlyway;
import org.kt.crm.model.Address;
import org.kt.crm.model.Client;
import org.kt.crm.model.Phone;
import org.kt.crm.service.DbServiceClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws InterruptedException {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var cacheClient = new MyCache<String, Client>();

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, cacheClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
///
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated"));
        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));

        System.gc();
        Thread.sleep(100);
    }
}
