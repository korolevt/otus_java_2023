package org.kt;

import com.google.gson.Gson;
import org.kt.crm.DBServiceClientFactory;
import org.kt.web.helpers.GsonHelper;
import org.kt.web.server.ClientsWebServerSimple;
import org.kt.web.server.ClientsWebServerWithFilterBasedSecurity;
import org.kt.web.services.TemplateProcessor;
import org.kt.web.services.TemplateProcessorImpl;
import org.kt.web.services.UserAuthService;
import org.kt.web.services.UserAuthServiceImpl;

/*
    // Стартовая страница
    http://localhost:8080  admin/12345678

    // Страница клиентов
    http://localhost:8080/clients

    // REST сервис
    GET http://localhost:8080/api/client/3 - получить клиента c Id=3
    POST http://localhost:8080/api/client  - добавить нового клиента
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var dbServiceClient = DBServiceClientFactory.getDbServiceClient(HIBERNATE_CFG_FILE);

        Gson gson = GsonHelper.createGson();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl();

        ClientsWebServerSimple clientsWebServer = new ClientsWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceClient, gson, templateProcessor);

        clientsWebServer.start();
        clientsWebServer.join();
    }
}
