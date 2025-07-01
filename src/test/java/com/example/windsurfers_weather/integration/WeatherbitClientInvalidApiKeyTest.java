package com.example.windsurfers_weather.integration;

import com.example.windsurfers_weather.client.WeatherbitClient;
import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = {
        "weatherbit.api.key=INVALID_KEY_123"
})
@DisplayName("WeatherbitClient: invalid API key")
public class WeatherbitClientInvalidApiKeyTest {

    @Autowired
    private WeatherbitClient weatherbitClient;

    @Test
    @DisplayName("Throws exception with INVALID_API_KEY when API key is wrong")
    void shouldThrowInvalidApiKey_WhenApiKeyIsInvalid() {
        double lat = 54.7084;
        double lon = 18.6554;

        WeatherDataUnavailableException exception = assertThrows(
                WeatherDataUnavailableException.class,
                () -> weatherbitClient.getForecast(lat, lon)
        );

        assertEquals(WeatherErrorReason.INVALID_API_KEY, exception.getReason());
    }
}

