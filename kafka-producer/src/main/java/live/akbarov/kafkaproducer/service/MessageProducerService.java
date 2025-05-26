package live.akbarov.kafkaproducer.service;

import live.akbarov.kafkaproducer.avro.MessageV1;
import live.akbarov.kafkaproducer.avro.MessageV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducerService {

    private final KafkaTemplate<String, MessageV1> kafkaTemplate1;
    private final KafkaTemplate<String, MessageV2> kafkaTemplate2;

    @Value("${kafka.topic.name}")
    private String topicName;

    public void sendMessage(String content) {
        MessageV1 message = MessageV1.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setContent(content)
                .setTimestamp(System.currentTimeMillis())
                .build();

        log.info("Sending message: {}", message);
        kafkaTemplate1.send(topicName, message.getId(), message)
                .whenComplete(doComplete(message));
    }

    public void sendMessage(String content, String priority) {
        MessageV2 message = MessageV2.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setContent(content)
                .setTimestamp(System.currentTimeMillis())
                .setPriority(priority)
                .build();

        log.info("Sending message v2: {}", message);
        kafkaTemplate2.send(topicName, message.getId(), message)
                .whenComplete(doComplete(message));
    }

    private static <T> BiConsumer<SendResult<String, T>, Throwable> doComplete(T message) {
        return (result, ex) -> {
            if (ex == null) {
                log.info("Message v2 sent successfully: {}", message);
                log.info("Partition: {}, Offset: {}",
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Unable to send message v2: {}", ex.getMessage());
            }
        };
    }
}
