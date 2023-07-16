package org.kt.Server;

import io.grpc.stub.StreamObserver;
import org.kt.protobuf.generated.NumberRequest;
import org.kt.protobuf.generated.NumberResponse;
import org.kt.protobuf.generated.NumberServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberServiceImpl extends NumberServiceGrpc.NumberServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(NumberServiceImpl.class);

    private static final int TIME_SLEEP = 2000;

    @Override
    public void generateNumbers(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        log.info("request: firstValue={}, lastValue={}", request.getFirstValue(), request.getLastValue());

        var executor = Executors.newSingleThreadScheduledExecutor();
        var number = new AtomicInteger(request.getFirstValue());

        Runnable task = () -> {
            var response = NumberResponse.newBuilder()
                    .setNumber(number.incrementAndGet())
                    .build();
            responseObserver.onNext(response);
            if (number.get() == request.getLastValue()) {
                executor.shutdown();
                responseObserver.onCompleted();
                log.info("onCompleted");
            }
        };

        executor.scheduleAtFixedRate(task, 0, TIME_SLEEP, TimeUnit.MILLISECONDS);
    }

/***
    // Реализация через sleep
    @Override
    public void generateNumbers(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        log.info("request: firstValue={}, lastValue={}", request.getFirstValue(), request.getLastValue());

        IntStream.range(request.getFirstValue(), request.getLastValue())
            .forEach(number -> {
                var response = NumberResponse.newBuilder()
                    .setNumber(number + 1)
                    .build();

                responseObserver.onNext(response);
                sleep();
            });
        responseObserver.onCompleted();
        log.info("onCompleted");
    }

    private void sleep() {
        try {
            Thread.sleep(TIME_SLEEP);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
***/
}
