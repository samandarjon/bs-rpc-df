package live.akbarov.grpcserver.service;

import io.grpc.stub.StreamObserver;
import live.akbarov.grpcserver.Message;
import live.akbarov.grpcserver.MessageServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Override
    public void doAction(Message request, StreamObserver<Message> observer) {
        logger.info("Message received {}", request);
        String responseMessage = "Pong";
        if (!"Ping".equals(request.getMessage())) {
            logger.warn("Received unexpected message: {}", request.getMessage());
        }
        observer.onNext(Message.newBuilder().setMessage(responseMessage).build());
        observer.onCompleted();
    }
}
