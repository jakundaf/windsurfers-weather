package com.example.windsurfers_weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherData {

    private double temperature;
    private double windSpeed;
    private double windDirection;

}
