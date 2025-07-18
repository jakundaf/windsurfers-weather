package com.example.windsurfers_weather.integration;

import com.example.windsurfers_weather.controller.MockWeatherController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@WebMvcTest(MockWeatherController.class)
class MockWeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnJastarnia_WhenProfileIsDev() throws Exception {
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", "2025-07-23"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location.name").value("Jastarnia"))
                .andExpect(jsonPath("$.data.temperature").value(20.0))
                .andExpect(jsonPath("$.data.windSpeed").value(10.0))
                .andExpect(jsonPath("$.data.windDirection").value(180.0))
                .andExpect(jsonPath("$.score").value(70.0));
    }
}
