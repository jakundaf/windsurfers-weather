package com.example.windsurfers_weather.exception.handler;

import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WeatherDataUnavailableException.class)
    public ResponseEntity<?> handleWeatherDataUnavailableException(WeatherDataUnavailableException ex) {
        HttpStatus status = mapErrorReasonToHttpStatus(ex.getReason());
        return ResponseEntity
                .status(status)
                .body(errorResponse("No data found", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        if (ex.getRequiredType() == java.time.LocalDate.class) {
            return ResponseEntity
                    .badRequest()
                    .body(errorResponse("Invalid date format", ex.getMessage()));
        }

        return ResponseEntity
                .badRequest()
                .body(errorResponse("Invalid parameter: ", ex.getMessage()));

    }

    @ExceptionHandler (MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException (MissingServletRequestParameterException ex){
        return ResponseEntity
                .badRequest()
                .body(errorResponse("No parameter passed", ex.getMessage()));
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
