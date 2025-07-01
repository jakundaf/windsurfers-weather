package com.example.windsurfers_weather.exception;


import com.example.windsurfers_weather.utility.WeatherErrorReason;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class WeatherDataUnavailableException extends RuntimeException {

    private LocalDate date;
    private final double lat;
    private final double lon;
    private final WeatherErrorReason reason;

    public WeatherDataUnavailableException( LocalDate date, double lat, double lon, WeatherErrorReason reason) {
        super(String.format("No weather data found for \n"
                + "lon: %.4f \n"
                + "lat: %.4f \n" + "in day %s \n"
                + "%s", lon, lat, date, reason));
        this.lat = lat;
        this.lon = lon;
        this.date = date;
        this.reason = reason;
    }

    public WeatherDataUnavailableException( double lat, double lon, WeatherErrorReason reason) {
        super(String.format("No weather data found for \n"
                + "lon: %.4f \n"
                + "lat: %.4f \n"
                + "%s", lon, lat, reason));
        this.lat = lat;
        this.lon = lon;
        this.reason = reason;
    }


}
