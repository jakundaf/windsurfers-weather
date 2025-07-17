package com.example.windsurfers_weather.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KafkaMessageDto {

    private String login;
    private String message;

}
