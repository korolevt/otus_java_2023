package org.kt.controllers;

import org.kt.domain.Client;
import org.kt.service.DBServiceClient;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientRestController {

    private final DBServiceClient dbServiceClient;

    public ClientRestController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/api/client/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return dbServiceClient.getClient(id).orElse(null);
    }

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody Client client) {
        return dbServiceClient.saveClient(client);
    }

}
