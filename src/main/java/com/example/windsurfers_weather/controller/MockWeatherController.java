package com.example.windsurfers_weather.controller;

import com.example.windsurfers_weather.dto.LocationWeatherResult;
import com.example.windsurfers_weather.dto.WeatherData;
import com.example.windsurfers_weather.entity.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@Profile("dev")
@RestController
@RequestMapping("/api/weather")
public class MockWeatherController {

    private static final int WIND_WEIGHT = 3;
    private static final double TEMP = 20.0;
    private static final double WIND_SPD = 10.0;
    private static final double WIND_DIR = 180.0;

    @GetMapping("/best-location")
    public ResponseEntity<LocationWeatherResult> getMockBestLocation(@RequestParam("date") LocalDate date) {
        log.info("GET /api/weather/best-location called with date={}, in DEV profile", date);

        Location location = new Location("Jastarnia", "Poland", 54.7084, 18.6554);
        WeatherData weather = new WeatherData(TEMP, WIND_SPD, WIND_DIR);
        double score = TEMP * WIND_WEIGHT + WIND_SPD;

        LocationWeatherResult result = new LocationWeatherResult(location, weather, score);

        return ResponseEntity.ok(result);
    }
}
