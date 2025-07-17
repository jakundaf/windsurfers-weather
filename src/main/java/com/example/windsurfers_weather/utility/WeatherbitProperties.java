package com.example.windsurfers_weather.utility;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "weatherbit")
@Data
public class WeatherbitProperties {
    private String apiUrl;
    private String apiKey;
}
