package org.kt.Client;

import io.grpc.ManagedChannelBuilder;
import org.kt.protobuf.generated.NumberRequest;
import org.kt.protobuf.generated.NumberServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NumbersClient {
    private static final Logger log = LoggerFactory.getLogger(NumbersClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private static final int SERVER_FIRST_VALUE = 0;
    private static final int SERVER_LAST_VALUE = 0;

    private static final int FIRST_VALUE = 0;
    private static final int LAST_VALUE = 50;

    private static final int TIME_SLEEP = 1000;

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = NumberServiceGrpc.newStub(channel);

        new NumbersClient().generate(stub);

        channel.shutdown();
    }

    private void generate(NumberServiceGrpc.NumberServiceStub stub) {
        var clientStreamObserver = new ClientStreamObserver();
        stub.generateNumbers(createNumberRequest(), clientStreamObserver);

        var currentValue = 0;
        for (int i = FIRST_VALUE; i < LAST_VALUE; i++) {
            currentValue =  currentValue
                    + clientStreamObserver.getValueAndReset() + 1;

            log.info("currentValue:{}", currentValue);
            sleep();
        }
    }

    private NumberRequest createNumberRequest() {
        return NumberRequest.newBuilder()
                .setFirstValue(SERVER_FIRST_VALUE)
                .setLastValue(SERVER_LAST_VALUE)
                .build();
    }

    private void sleep() {
        try {
            Thread.sleep(TIME_SLEEP);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
