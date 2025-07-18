package com.example.windsurfers_weather.integration;

import com.example.windsurfers_weather.client.WeatherbitClient;
import com.example.windsurfers_weather.exception.WeatherApiException;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherbitClient weatherbitClient;

    @Test
    @DisplayName("Returns 401 when API returns UNAUTHORIZED")
    void shouldReturn401_whenUnauthorized() throws Exception {

        // Arrange
        when(weatherbitClient.getForecast(anyDouble(), anyDouble()))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        LocalDate date = LocalDate.now();

        // Act & Assert
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", date.toString()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Weather API error"))
                .andExpect(jsonPath("$.message").value("Weather API reason: INVALID_API_KEY"));
    }

    @Test
    @DisplayName("Returns 401 when API returns FORBIDDEN")
    void shouldReturn403_whenForbidden() throws Exception {

        // Arrange
        when(weatherbitClient.getForecast(anyDouble(), anyDouble()))
                .thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        LocalDate date = LocalDate.now();

        // Act & Assert
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", date.toString()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Weather API error"))
                .andExpect(jsonPath("$.message").value("Weather API reason: INVALID_API_KEY"));
    }

    @Test
    @DisplayName("Returns 404 when API returns BAD REQUEST")
    void shouldReturn404_whenBadRequest() throws Exception {

        // Arrange
        when(weatherbitClient.getForecast(anyDouble(), anyDouble()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        LocalDate date = LocalDate.now();

        // Act & Assert
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", date.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Weather API reason: NO_DATA_FOR_DATE"));
    }

    @Test
    @DisplayName("Returns 503 when API IS UNREACHABLE (500, timeout, gateway error)")
    void shouldReturn503_whenServerError() throws Exception {

        // Arrange
        when(weatherbitClient.getForecast(anyDouble(), anyDouble()))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        LocalDate date = LocalDate.now();

        // Act & Assert
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", date.toString()))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("Weather API reason: API_UNREACHABLE"));
    }

    @Test
    @DisplayName("Returns 503 when API throws ResourceAccessException (ie. timeout)")
    void shouldReturn503_whenConnectionTimeout() throws Exception {

        // Arrange
        when(weatherbitClient.getForecast(anyDouble(), anyDouble()))
                .thenThrow(new ResourceAccessException("Connection refused"));

        LocalDate date = LocalDate.now();

        // Act & Assert
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", date.toString()))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("Weather API reason: API_UNREACHABLE"));
    }

    @Test
    @DisplayName("Returns 500 when RUNTIME ERROR")
    void shouldReturn500_whenUnexpectedError() throws Exception {

        // Arrange
        when(weatherbitClient.getForecast(anyDouble(), anyDouble()))
                .thenThrow(new RuntimeException("Unexpected error"));

        LocalDate date = LocalDate.now();

        // Act & Assert
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", date.toString()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Weather API reason: UNKNOWN"));
    }

    @Test
    @DisplayName("Returns proper status when handler gets WeatherApiException")
    void shouldMapWeatherApiExceptionCorrectly() throws Exception {

        // Arrange
        when(weatherbitClient.getForecast(anyDouble(), anyDouble()))
                .thenThrow(new WeatherApiException(WeatherErrorReason.NO_DATA_FOR_DATE));

        LocalDate date = LocalDate.now();

        // Act & Assert
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", date.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Weather API reason: NO_DATA_FOR_DATE"));
    }
}
