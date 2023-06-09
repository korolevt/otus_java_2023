package org.kt;

import com.google.gson.Gson;
import org.kt.crm.model.Address;
import org.kt.crm.model.Client;
import org.kt.crm.model.Phone;
import org.kt.web.helpers.GsonHelper;

import java.util.Arrays;

public class GsonTest {
    public static void main(String[] args) {
        Gson gson = GsonHelper.createGson();

        var clientFirst = new Client(3L,"Клиент с адресом и телефоном", new Address("ул. Строителей"),
                Arrays.asList(new Phone("123"), new Phone("456")));

        String json = gson.toJson(clientFirst);

        System.out.println(json);
    }


}
