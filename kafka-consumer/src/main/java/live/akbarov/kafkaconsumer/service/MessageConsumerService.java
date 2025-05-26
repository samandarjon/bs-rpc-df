package live.akbarov.kafkaconsumer.service;


import live.akbarov.kafkaproducer.avro.MessageV1;
import live.akbarov.kafkaproducer.avro.MessageV2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumerService {
    private static final Logger log = LoggerFactory.getLogger(MessageConsumerService.class);

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
