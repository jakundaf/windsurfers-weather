package com.example.windsurfers_weather.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessageDto {

    @NotEmpty
    @NotBlank(message = "Login is required")
    private String login;

    @NotEmpty
    @NotBlank(message = "Message is required")
    private String message;

}
