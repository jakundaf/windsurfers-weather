package com.example.windsurfers_weather.controller;

import com.example.windsurfers_weather.dto.KafkaMessageDto;
import com.example.windsurfers_weather.service.KafkaProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/kafka")
public class KafkaMessageController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    public ResponseEntity<String> sendToKafka(@Valid @RequestBody KafkaMessageDto dto) {
        log.info("POST /api/kafka/send called with login={} and message={}", dto.getLogin(), dto.getMessage());

        String payload = String.format("Login: %s, Message: %s", dto.getLogin(), dto.getMessage());
        kafkaProducerService.sendMessage(payload);
        return ResponseEntity.ok("Message sent to Kafka.");
    }
}
