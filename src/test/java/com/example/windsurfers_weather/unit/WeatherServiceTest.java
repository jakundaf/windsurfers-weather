package com.example.windsurfers_weather.unit;

import com.example.windsurfers_weather.client.WeatherbitClient;
import com.example.windsurfers_weather.dto.LocationWeatherResult;
import com.example.windsurfers_weather.dto.WeatherbitResponse;
import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import com.example.windsurfers_weather.service.WeatherService;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    private WeatherbitClient weatherbitClientMock;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherbitClientMock = mock(WeatherbitClient.class);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        weatherService = new WeatherService(weatherbitClientMock, decimalFormat);
    }

    @Test
    @DisplayName("Returns locations list correctly")
    void shouldReturnStaticLocations() {
        List<?> locations = weatherService.getLocations();
        assertNotNull(locations);
        assertEquals(5, locations.size());
    }

    @Nested
    @DisplayName("getBestLocationForSurfing")
    class GetBestLocationForSurfingTests {

        @Test
        @DisplayName("Returns best location when all forecasts succeed")
        void shouldReturnBestLocation_WhenAllForecastsAvailable() {

            // Arrange
            LocalDate date = LocalDate.now().plusDays(1);
            mockForecast("Jastarnia", 12.0f, 70.0f);
            mockForecast("Bridgetown", 9.0f, 50.0f);
            mockForecast("Fortaleza", 7.0f, 40.0f);
            mockForecast("Pissouri", 5.5f, 30.0f);
            mockForecast("Le Morne", 6.0f, 20.0f);

            // Act
            Optional<LocationWeatherResult> result = weatherService.getBestLocation(date);

            // Assert
            assertNotNull(result);
            assertTrue(result.isPresent());
            assertEquals("Jastarnia", result.get().getLocation().getName());
        }

        @Test
        @DisplayName("Skips locations when forecast fails for them")
        void shouldSkipLocation_WhenWeatherDataUnavailable() {

            // Arrange
            LocalDate date = LocalDate.now().plusDays(1);
            mockForecast("Jastarnia", 11.0f, 70.0f);
            mockForecast("Fortaleza", 15.0f, 40.0f);
            mockForecast("Pissouri", 14.0f, 30.0f);
            mockForecast("Le Morne", 13.0f, 20.0f);

            doThrow(new WeatherDataUnavailableException(date, 13.1035, -59.6032, WeatherErrorReason.NO_DATA_FOR_DATE))
                    .when(weatherbitClientMock).getForecast(eq(13.1035), eq(-59.6032));

            // Act
            Optional<LocationWeatherResult> result = weatherService.getBestLocation(date);

            // Assert
            assertNotNull(result);
            assertTrue(result.isPresent());
            assertNotEquals("Bridgetown", result.get().getLocation().getName());
            verify(weatherbitClientMock, times(5)).getForecast(anyDouble(), anyDouble());
        }

        @Test
        @DisplayName("Returns null when all forecasts fail")
        void shouldReturnNull_WhenAllForecastsFail() {

            // Arrange
            LocalDate date = LocalDate.now().plusDays(1);

            for (var loc : weatherService.getLocations()) {
                doThrow(new WeatherDataUnavailableException(date, loc.getLat(), loc.getLon(), WeatherErrorReason.UNKNOWN))
                        .when(weatherbitClientMock)
                        .getForecast(eq(loc.getLat()), eq(loc.getLon()));
            }

            // Act
            Optional<LocationWeatherResult> result = weatherService.getBestLocation(date);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Returns first location when multiple have the same best score")
        void shouldReturnFirstOfEqualScores() throws WeatherDataUnavailableException {

            // Arrange
            LocalDate date = LocalDate.now().plusDays(1);
            mockForecast("Jastarnia", 10.0f, 30.0f);
            mockForecast("Bridgetown", 10.0f, 30.0f);
            mockForecast("Fortaleza", 10.0f, 30.0f);
            mockForecast("Pissouri", 5.0f, 70.0f);
            mockForecast("Le Morne", 5.0f, 70.0f);

            // Act
            Optional<LocationWeatherResult> result = weatherService.getBestLocation(date);

            // Assert
            assertTrue(result.isPresent());
            assertEquals("Jastarnia", result.get().getLocation().getName());
        }

        @Test
        @DisplayName("Returns null when passed data is invalid")
        void shouldReturnNull_WhenDataIsInvalid() {

            // Arrange
            LocalDate date = LocalDate.now().plusDays(20);
            mockForecast("Jastarnia", 10.0f, 30.0f);
            mockForecast("Bridgetown", 10.0f, 30.0f);
            mockForecast("Fortaleza", 10.0f, 30.0f);
            mockForecast("Pissouri", 5.0f, 70.0f);
            mockForecast("Le Morne", 5.0f, 70.0f);

            // Act
            Optional<LocationWeatherResult> result = weatherService.getBestLocation(date);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }


        private void mockForecast(String city, double windSpeed, double windDirection) throws WeatherDataUnavailableException {
            var location = weatherService.getLocations().stream()
                    .filter(l -> l.getName().equals(city))
                    .findFirst()
                    .orElseThrow();

            WeatherbitResponse.ForecastDay forecastDay = new WeatherbitResponse.ForecastDay();
            forecastDay.setDatetime(LocalDate.now().plusDays(1));
            forecastDay.setTemp(10.0f);
            forecastDay.setWind_spd(windSpeed);
            forecastDay.setWind_dir(windDirection);

            WeatherbitResponse response = new WeatherbitResponse();
            response.setForecastDayList(List.of(forecastDay));

            when(weatherbitClientMock.getForecast(location.getLat(), location.getLon()))
                    .thenReturn(response);
        }
    }
}

