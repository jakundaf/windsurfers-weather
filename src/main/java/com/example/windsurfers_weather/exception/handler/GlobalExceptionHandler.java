package com.example.windsurfers_weather.exception.handler;

import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WeatherDataUnavailableException.class)
    public ResponseEntity<?> handleWeatherDataUnavailableException(WeatherDataUnavailableException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse("No data found", ex.getMessage()));
    }

    private Map<String, Object> errorResponse(String title, String detail){
        return Map.of(
                "timestamp", LocalDate.now(),
                "error", title,
                "message", detail
        );
    }


}
