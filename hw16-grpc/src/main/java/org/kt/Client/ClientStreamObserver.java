package org.kt.Client;

import io.grpc.stub.StreamObserver;
import org.kt.protobuf.generated.NumberResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientStreamObserver implements StreamObserver<NumberResponse> {
    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);

    private final AtomicInteger serverValue = new AtomicInteger(0);

    @Override
    public void onNext(NumberResponse numberResponse) {
        serverValue.set(numberResponse.getNumber());
        log.info("число от сервера: " + numberResponse.getNumber());
    }

    @Override
    public void onError(Throwable t) {
        log.error(t.getMessage());
    }

    @Override
    public void onCompleted() {
        log.debug("Complete");
    }

    public int getValueAndReset() {
        return serverValue.getAndSet(0);
    }
}
