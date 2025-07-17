package com.example.windsurfers_weather.exception;

import com.example.windsurfers_weather.utility.WeatherErrorReason;
import lombok.Data;

@Data
public class WeatherApiException extends RuntimeException {

    private final WeatherErrorReason reason;

    public WeatherApiException(WeatherErrorReason reason) {
        super("Weather API reason: " + reason);
        this.reason = reason;
    }
}
