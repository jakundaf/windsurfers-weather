package com.example.windsurfers_weather.client;

import com.example.windsurfers_weather.dto.WeatherbitResponse;
import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import com.example.windsurfers_weather.utility.WeatherbitProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Slf4j
@Component
public class WeatherbitClient {

    private static final String PARAM_LAT = "lat";
    private static final String PARAM_LON = "lon";
    private static final String PARAM_KEY = "key";


    private final WeatherbitProperties weatherbitProperties;
    private final RestTemplate restTemplate;

    public WeatherbitClient(RestTemplate restTemplate, WeatherbitProperties weatherbitProperties) {
        this.restTemplate = restTemplate;
        this.weatherbitProperties = weatherbitProperties;
    }

    public WeatherbitResponse getForecast(double lat, double lon) {

        String url = UriComponentsBuilder.fromUriString(weatherbitProperties.getApiUrl())
                .queryParam(PARAM_LAT, lat)
                .queryParam(PARAM_LON, lon)
                .queryParam(PARAM_KEY, weatherbitProperties.getApiKey())
                .toUriString();

        log.info("Requesting weather forecast for lat={}, lon={}", lat, lon);

            WeatherbitResponse response = restTemplate.getForObject(url, WeatherbitResponse.class);

            if (response == null || response.getForecastDayList() == null) {
                log.warn("Empty response from Weatherbit API for lat={}, lon={}", lat, lon);
                throw new WeatherDataUnavailableException(lat, lon, WeatherErrorReason.API_UNREACHABLE);
            }
            return response;
    }
}
