package com.example.windsurfers_weather.exception;


import com.example.windsurfers_weather.utility.WeatherErrorReason;
import lombok.Getter;

import java.time.LocalDate;


@Getter
public class WeatherDataUnavailableException extends RuntimeException {

    private LocalDate date;
    private final double lat;
    private final double lon;
    private final WeatherErrorReason reason;

    public WeatherDataUnavailableException(LocalDate date, double lat, double lon, WeatherErrorReason reason) {
        super(String.format("No weather data found for "
                + "lon: %.4f "
                + "lat: %.4f " + "in day %s, "
                + "%s", lon, lat, date, reason));
        this.lat = lat;
        this.lon = lon;
        this.date = date;
        this.reason = reason;
    }

    public WeatherDataUnavailableException(double lat, double lon, WeatherErrorReason reason) {
        super(String.format("No weather data found for "
                + "lon: %.4f "
                + "lat: %.4f,"
                + "%s", lon, lat, reason));
        this.lat = lat;
        this.lon = lon;
        this.reason = reason;
    }


}
