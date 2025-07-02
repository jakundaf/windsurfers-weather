package com.example.windsurfers_weather.dto;

import com.example.windsurfers_weather.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationWeatherResult {

    private Location location;
    private WeatherData data;
    private double score;

}
