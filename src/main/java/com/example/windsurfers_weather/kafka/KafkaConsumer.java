package com.example.windsurfers_weather.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "weather-topic")
    public void listen(ConsumerRecord<String, String> record){
        log.info("Message from Kafka received: {}", record.value());
    }
}
