package org.kt.controllers;

import org.kt.domain.Address;
import org.kt.domain.Client;
import org.kt.domain.Phone;
import org.kt.service.DBServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientController {

    private final DBServiceClient dbServiceClient;

    ClientController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsView(Model model) {
        List<Client> clients = dbServiceClient.findAll();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@RequestParam(name="name") String name,
                                   @RequestParam(name="street") String street,
                                   @RequestParam(name="number") String number) {
        Address address = null;
        List<Phone> phones = null;
        if (!street.isEmpty()) {
            address = new Address(street);
        }
        if (!number.isEmpty()) {
            phones = new ArrayList<>();
            phones.add(new Phone(number));
        }
        var client = new Client(name, address, phones);
        dbServiceClient.saveClient(client);
        return new RedirectView("/", true);
    }
}
