package com.example.windsurfers_weather.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DecimalFormat;

@Configuration
public class WeatherConfig {

    @Bean
    public DecimalFormat decimalFormat() {
        return new DecimalFormat("#.##");
    }
}
