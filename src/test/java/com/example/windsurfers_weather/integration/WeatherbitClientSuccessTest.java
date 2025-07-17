package com.example.windsurfers_weather.integration;


import com.example.windsurfers_weather.client.WeatherbitClient;
import com.example.windsurfers_weather.dto.WeatherbitResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("WeatherbitClient: valid request")
public class WeatherbitClientSuccessTest {

    @Autowired
    private WeatherbitClient weatherbitClient;

    @Test
    @DisplayName("Returns forecast when lat/lon and API key are valid")
    void shouldReturnForecast_WhenLatAndLonAndApiKeyAreValid() {

        // Arrange
        double lat = 54.7084;
        double lon = 18.6554;

        // Act
        WeatherbitResponse response = weatherbitClient.getForecast(lat, lon);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getForecastDayList());
        assertFalse(response.getForecastDayList().isEmpty());
    }
}

