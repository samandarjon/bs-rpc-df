package live.akbarov.kafkaconsumer.service;


import live.akbarov.kafkaproducer.avro.MessageV1;
import live.akbarov.kafkaproducer.avro.MessageV2;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageConsumerService {

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, Object> message) {
        log.info("Received message: {}", message);
        if (message.value() instanceof MessageV2 v2) {
            log.info("Received message v2: {}", v2);
        }

        if (message.value() instanceof MessageV1 v1) {
            log.info("Received message v1: {}", v1);
        }

    }


}
