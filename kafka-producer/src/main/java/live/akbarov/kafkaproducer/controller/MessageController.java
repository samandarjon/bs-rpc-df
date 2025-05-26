package live.akbarov.kafkaproducer.controller;

import live.akbarov.kafkaproducer.service.MessageProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageProducerService producerService;

    @PostMapping("/v1")
    public ResponseEntity<String> sendMessageV1(@RequestParam String content) {
        log.info("Received request to send message v1: {}", content);
        producerService.sendMessage(content);
        return ResponseEntity.ok("Message sent successfully");
    }

    @PostMapping("/v2")
    public ResponseEntity<String> sendMessageV2(
            @RequestParam String content,
            @RequestParam(required = false) String priority) {
        log.info("Received request to send message v2: {}, priority: {}", content, priority);
        producerService.sendMessage(content, priority);
        return ResponseEntity.ok("Message v2 sent successfully");
    }
}