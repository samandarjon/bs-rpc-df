package live.akbarov.grpcserver.service;

import io.grpc.stub.StreamObserver;
import live.akbarov.grpcserver.Message;
import live.akbarov.grpcserver.MessageServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

import java.util.Optional;

@GrpcService
public class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Override
    public void doAction(Message request, StreamObserver<Message> observer) {
        logger.info("Message received {}", request);

        Optional.of(request)
                .map(Message::getMessage)
                .filter("Ping"::equals)
                .ifPresentOrElse(
                        message -> observer.onNext(Message.newBuilder().setMessage("Pong").build()),
                        () -> logger.warn("Received unexpected message: {}", request));
        observer.onCompleted();
    }
}
