package live.akbarov.grpcserver.service;

import io.grpc.stub.StreamObserver;
import live.akbarov.grpcserver.Message;
import live.akbarov.grpcserver.MessageServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

import java.util.Optional;

@Slf4j
@GrpcService
public class MessageService extends MessageServiceGrpc.MessageServiceImplBase {

    @Override
    public void doAction(Message request, StreamObserver<Message> observer) {
        log.info("Message received {}", request);

        Optional.of(request)
                .map(Message::getMessage)
                .filter("Ping"::equals)
                .ifPresentOrElse(
                        message -> observer.onNext(Message.newBuilder().setMessage("Pong").build()),
                        () -> log.warn("Received unexpected message: {}", request));
        observer.onCompleted();
    }
}
