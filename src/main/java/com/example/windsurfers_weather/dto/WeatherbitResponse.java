package com.example.windsurfers_weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherbitResponse {

    @JsonProperty("data")
    private List<ForecastDay> forecastDayList;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ForecastDay {
        private LocalDate datetime;
        private double lon;
        private double lan;
        private float wind_spd;
        private float wind_dir;
        private float temp;
    }


}
