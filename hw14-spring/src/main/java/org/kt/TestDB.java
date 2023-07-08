package org.kt;

import org.kt.domain.Address;
import org.kt.domain.Client;
import org.kt.domain.Phone;
import org.kt.service.DBServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;


//@Component("testDb")
public class TestDB implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TestDB.class);

    private final DBServiceClient dbServiceClient;


    public TestDB(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public void run(String... args) {
/*        var clientFirst = dbServiceClient.getClient(5).get();
        clientFirst.getPhones().add(new Phone("789"));
        dbServiceClient.saveClient(clientFirst); */
        var clientFirst = new Client("Клиент с адресом и телефоном", new Address("ул. Строителей"),
                Arrays.asList(new Phone("123"), new Phone("456")));
        clientFirst = dbServiceClient.saveClient(clientFirst);
        var clientSecond = new Client("Клиент без телефона", new Address("ул. Безымянная"), null);
        dbServiceClient.saveClient(clientSecond);
        var clientThird = new Client("Клиент без адреса", null, null);
        dbServiceClient.saveClient(clientThird);
        var clientForth = new Client(null, null, null);
        dbServiceClient.saveClient(clientForth);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
