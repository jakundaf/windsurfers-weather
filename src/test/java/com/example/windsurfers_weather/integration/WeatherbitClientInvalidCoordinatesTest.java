package com.example.windsurfers_weather.integration;

import com.example.windsurfers_weather.client.WeatherbitClient;
import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("WeatherbitClient: invalid coordinates")
public class WeatherbitClientInvalidCoordinatesTest {

    @Autowired
    private WeatherbitClient weatherbitClient;

    @Test
    @DisplayName("Throws exception with INVALID_COORDINATES when lat/lon are invalid")
    void shouldThrowInvalidCoordinates_WhenLatAndLonAreInvalid() {
        double lat = -999999;
        double lon = 999999;

        WeatherDataUnavailableException exception = assertThrows(
                WeatherDataUnavailableException.class,
                () -> weatherbitClient.getForecast(lat, lon)
        );

        assertEquals(WeatherErrorReason.INVALID_COORDINATES, exception.getReason());
    }
}

