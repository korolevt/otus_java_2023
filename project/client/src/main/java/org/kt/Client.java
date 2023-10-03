package org.kt;

import org.kt.listener.WebSocketSoundListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Client {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Client.class, args);


//        ctx.getBean(WebSocketSoundListener.class).connect();
    }
}