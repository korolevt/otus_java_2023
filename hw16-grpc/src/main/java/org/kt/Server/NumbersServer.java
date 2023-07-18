package org.kt.Server;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class NumbersServer {
    private static final Logger log = LoggerFactory.getLogger(NumbersServer.class);
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var numberService = new NumberServiceImpl();

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(numberService).build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( ()-> {
            server.shutdown();
            log.info("Server shutdown");
        }));

        log.info("server waiting for client connections...");
        server.awaitTermination();
    }
}
