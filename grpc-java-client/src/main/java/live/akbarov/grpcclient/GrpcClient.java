package live.akbarov.grpcclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import live.akbarov.grpcserver.Message;
import live.akbarov.grpcserver.MessageServiceGrpc;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class GrpcClient implements AutoCloseable {
    private final ManagedChannel channel;
    private final MessageServiceGrpc.MessageServiceBlockingStub blockingStub;

    public GrpcClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build());
    }

    GrpcClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = MessageServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }

    public void sendMessage(String text) {
        log.info("Sending message: {}", text);
        Message request = Message.newBuilder().setMessage(text).build();
        Message response;
        try {
            response = blockingStub.doAction(request);
            log.info("Response received: {}", response.getMessage());
        } catch (Exception e) {
            log.error("RPC failed: {}", e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        try (GrpcClient client = new GrpcClient("localhost", 8080)) {
            client.sendMessage("Ping");
        } catch (RuntimeException e) {
            log.error("Unexpected error occupied: {}", e.getMessage());
        }
    }
}
