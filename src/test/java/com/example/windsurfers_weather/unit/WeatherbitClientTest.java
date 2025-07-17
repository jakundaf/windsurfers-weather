package com.example.windsurfers_weather.unit;

import com.example.windsurfers_weather.client.WeatherbitClient;
import com.example.windsurfers_weather.dto.WeatherbitResponse;
import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import com.example.windsurfers_weather.utility.WeatherbitProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherbitClientTest {

    private WeatherbitClient weatherbitClient;
    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private WeatherbitProperties weatherbitProperties;

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        weatherbitClient = new WeatherbitClient(restTemplateMock,weatherbitProperties);
        when(weatherbitProperties.getApiUrl()).thenReturn("http://mocked-url.com");
        when(weatherbitProperties.getApiKey()).thenReturn("mock-api-key");
    }


    @Test
    @DisplayName("Returns forecast when API responds successfully")
    void shouldReturnForecast_whenApiRespondsSuccessfully() {

        // Arrange
        WeatherbitResponse mockResponse = new WeatherbitResponse();
        mockResponse.setForecastDayList(List.of(new WeatherbitResponse.ForecastDay()));

        when(restTemplateMock.getForObject(anyString(), eq(WeatherbitResponse.class)))
                .thenReturn(mockResponse);

        // Act
        WeatherbitResponse result = weatherbitClient.getForecast(1.0, 2.0);

        // Assert
        assertNotNull(result);
        assertFalse(result.getForecastDayList().isEmpty());
    }


    @Test
    @DisplayName("Throws API_UNREACHABLE when response is null")
    void shouldThrowApiUnreachable_whenResponseIsNull() {

        // Arrange
        when(restTemplateMock.getForObject(anyString(), eq(WeatherbitResponse.class)))
                .thenReturn(null);

        // Act & Assert
        WeatherDataUnavailableException exception = assertThrows(
                WeatherDataUnavailableException.class,
                () -> weatherbitClient.getForecast(1.0, 2.0)
        );

        assertEquals(WeatherErrorReason.API_UNREACHABLE, exception.getReason());
    }

    @Test
    @DisplayName("Throws API_UNREACHABLE when forecast list is null")
    void shouldThrowApiUnreachable_whenForecastListIsNull() {

        // Arrange
        WeatherbitResponse emptyResponse = new WeatherbitResponse();
        emptyResponse.setForecastDayList(null);

        when(restTemplateMock.getForObject(anyString(), eq(WeatherbitResponse.class)))
                .thenReturn(emptyResponse);

        // Act & Arrange
        WeatherDataUnavailableException exception = assertThrows(
                WeatherDataUnavailableException.class,
                () -> weatherbitClient.getForecast(1.0, 2.0)
        );

        assertEquals(WeatherErrorReason.API_UNREACHABLE, exception.getReason());
    }

}
