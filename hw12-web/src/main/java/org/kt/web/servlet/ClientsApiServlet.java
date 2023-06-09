package org.kt.web.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kt.crm.model.Address;
import org.kt.crm.model.Client;
import org.kt.crm.model.Phone;
import org.kt.crm.service.DBServiceClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceClient serviceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient serviceClient, Gson gson) {
        this.serviceClient = serviceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Address address = null;
        List<Phone> phones = null;

        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String street = request.getParameter("street");
        String number = request.getParameter("number");
        if (!street.isEmpty()) {
            address = new Address(street);
        }
        if (!number.isEmpty()) {
            phones = new ArrayList<>();
            phones.add(new Phone(number));
        }
        var client = new Client(null, name, address, phones);
        serviceClient.saveClient(client);

        String path = request.getContextPath() + "/clients";
        response.sendRedirect(path);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Client client = serviceClient.getClient(extractIdFromRequest(request)).orElse(null);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(client));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

}
