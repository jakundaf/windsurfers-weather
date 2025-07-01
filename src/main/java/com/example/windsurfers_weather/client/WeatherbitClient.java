package com.example.windsurfers_weather.client;

import com.example.windsurfers_weather.dto.WeatherbitResponse;
import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WeatherbitClient {

    private final String apiKey;
    private final String apiUrl;

    private final RestTemplate restTemplate;

    public WeatherbitClient(RestTemplate restTemplate, @Value("${weatherbit.api.url}") String apiUrl,
    @Value("${weatherbit.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public WeatherbitResponse getForecast(double lat, double lon) {

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("key", apiKey)
                .toUriString();

        try {
            WeatherbitResponse response = restTemplate.getForObject(url, WeatherbitResponse.class);

            if (response == null || response.getForecastDayList() == null) {
                throw new WeatherDataUnavailableException(lat, lon, WeatherErrorReason.API_UNREACHABLE);
            }
            return response;

        } catch (WeatherDataUnavailableException e) {
            throw e;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatusCode status = e.getStatusCode();

            if (status == HttpStatus.BAD_REQUEST){
                throw new WeatherDataUnavailableException(lat, lon, WeatherErrorReason.INVALID_COORDINATES);

            } else if (status == HttpStatus.UNAUTHORIZED || status == HttpStatus.FORBIDDEN){
                throw new WeatherDataUnavailableException(lat, lon, WeatherErrorReason.INVALID_API_KEY);

            } else if (status == HttpStatus.INTERNAL_SERVER_ERROR || status == HttpStatus.BAD_GATEWAY
                || status == HttpStatus.SERVICE_UNAVAILABLE || status == HttpStatus.GATEWAY_TIMEOUT) {
                throw new WeatherDataUnavailableException(lat, lon, WeatherErrorReason.API_UNREACHABLE);

            } else {
                throw new WeatherDataUnavailableException(lat, lon, WeatherErrorReason.UNKNOWN);

            }
        } catch (ResourceAccessException e) {
            throw new WeatherDataUnavailableException(lat, lon, WeatherErrorReason.API_UNREACHABLE);

        }
        catch (Exception e){
            throw new WeatherDataUnavailableException(lat, lon, WeatherErrorReason.UNKNOWN);

        }
    }
}
