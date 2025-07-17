package com.example.windsurfers_weather.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducerService {

    private static final String TOPIC = "weather-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;


    public void sendMessage(String message){
        log.info("Sending message to Kafka: {}", message);
        kafkaTemplate.send(TOPIC, message);
    }

}
