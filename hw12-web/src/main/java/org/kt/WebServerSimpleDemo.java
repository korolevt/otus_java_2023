package org.kt;

import com.google.gson.Gson;
import org.kt.crm.DBServiceClientFactory;
import org.kt.web.helpers.GsonHelper;
import org.kt.web.server.ClientsWebServer;
import org.kt.web.server.ClientsWebServerSimple;
import org.kt.web.services.TemplateProcessor;
import org.kt.web.services.TemplateProcessorImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница клиентов
    http://localhost:8080/clients

    // REST сервис
    получить
    GET http://localhost:8080/api/client/3
    добавить
    POST http://localhost:8080/api/client
*/
public class WebServerSimpleDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var dbServiceClient = DBServiceClientFactory.getDbServiceClient(HIBERNATE_CFG_FILE);
        Gson gson = GsonHelper.createGson();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        ClientsWebServer clientsWebServer = new ClientsWebServerSimple(WEB_SERVER_PORT, dbServiceClient,
                gson, templateProcessor);

        clientsWebServer.start();
        clientsWebServer.join();
    }


}
