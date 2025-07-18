package com.example.windsurfers_weather.exception.handler;

import com.example.windsurfers_weather.exception.WeatherApiException;
import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WeatherDataUnavailableException.class)
    public ResponseEntity<?> handleWeatherDataUnavailableException(WeatherDataUnavailableException ex) {
        log.warn("Weather data unavailable exception occurred: {}", ex.getReason());
        HttpStatus status = mapErrorReasonToHttpStatus(ex.getReason());
        return ResponseEntity
                .status(status)
                .body(errorResponse("No data found", ex.getMessage()));
    }

    @ExceptionHandler(WeatherApiException.class)
    public ResponseEntity<?> handleWeatherApiException(WeatherApiException ex) {
        log.warn("Weather API exception occurred: {}", ex.getReason());
        HttpStatus status = mapErrorReasonToHttpStatus(ex.getReason());
        return ResponseEntity
                .status(status)
                .body(errorResponse("Weather API error", ex.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleClientError(HttpClientErrorException e) {
        log.warn("Weather client error exception occurred: {}", e.getMessage());
        WeatherErrorReason reason = switch (e.getStatusCode()) {
            case BAD_REQUEST -> WeatherErrorReason.NO_DATA_FOR_DATE;
            case UNAUTHORIZED, FORBIDDEN -> WeatherErrorReason.INVALID_API_KEY;
            default -> WeatherErrorReason.UNKNOWN;
        };

        return handleWeatherApiException(new WeatherApiException(reason));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<?> handleServerError(HttpServerErrorException e) {
        log.warn("Weather server error exception occurred: {}", e.getMessage());
        return handleWeatherApiException(new WeatherApiException(WeatherErrorReason.API_UNREACHABLE));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<?> handleTimeout(ResourceAccessException e) {
        log.warn("Weather timeout exception occurred: {}", e.getMessage());
        return handleWeatherApiException(new WeatherApiException(WeatherErrorReason.API_UNREACHABLE));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeError(RuntimeException e) {
        log.warn("Weather runtime error exception occurred: {}", e.getMessage());
        return handleWeatherApiException(new WeatherApiException(WeatherErrorReason.UNKNOWN));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleArgumentMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("Invalid method argument: {}", e.getMessage());
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDate.now(),
                "error", "Invalid parameter",
                "message", "Invalid format for '" + e.getName() + "' : '" + e.getValue() + "'"
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleArgumentValidation(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDate.now(),
                "error", "Validation error",
                "message", "Try passing the arguments again"
        ));
    }


    private Map<String, Object> errorResponse(String title, String detail) {
        return Map.of(
                "timestamp", LocalDate.now(),
                "error", title,
                "message", detail
        );
    }

    private HttpStatus mapErrorReasonToHttpStatus(WeatherErrorReason reason) {
        return switch (reason) {
            case INVALID_API_KEY -> HttpStatus.UNAUTHORIZED;
            case API_UNREACHABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            case NO_DATA_FOR_DATE -> HttpStatus.NOT_FOUND;
            case UNKNOWN -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }


}
