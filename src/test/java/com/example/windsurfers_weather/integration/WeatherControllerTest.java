package com.example.windsurfers_weather.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Returns 200 and correct body when date is valid and suitable location found")
    void shouldReturnBestLocation_WhenDataIsValidAndLocationFound() throws Exception {

        // Arrange
        LocalDate date = LocalDate.now().plusDays(1);

        // Act & Assert
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.score").exists());
    }

    @Test
    @DisplayName("Returns 204 No Content when no suitable location found - date out of 16 days range")
    void shouldReturnNoContent_WhenNoDataAvailable() throws Exception {

        // Arrange
        String date = LocalDate.now().plusDays(20).toString();

        // Act & Assert
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Returns 400 Bad Request when date is not in ISO format")
    void shouldReturnBadRequest_WhenInvalidDateFormat() throws Exception {

        // Act & Assert
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", "invalid-date")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}
