package org.kt;

import org.kt.crm.DBServiceClientFactory;
import org.kt.crm.model.Address;
import org.kt.crm.model.Client;
import org.kt.crm.model.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var dbServiceClient = DBServiceClientFactory.getDbServiceClient(HIBERNATE_CFG_FILE);

        var clientFirst = new Client(null,"Клиент с адресом и телефоном", new Address("ул. Строителей"),
                Arrays.asList(new Phone("123"), new Phone("456")));
        dbServiceClient.saveClient(clientFirst);
        var clientSecond = new Client(null,"Клиент без телефона", new Address("ул. Безымянная"));
        dbServiceClient.saveClient(clientSecond);
        var clientThird = new Client(null,"Клиент без адреса");
        dbServiceClient.saveClient(clientThird);
        var clientForth = new Client(null,null);
        dbServiceClient.saveClient(clientForth);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}