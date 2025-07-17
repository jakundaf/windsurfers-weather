package com.example.windsurfers_weather.service;

import com.example.windsurfers_weather.client.WeatherbitClient;
import com.example.windsurfers_weather.dto.LocationWeatherResult;
import com.example.windsurfers_weather.dto.WeatherData;
import com.example.windsurfers_weather.dto.WeatherbitResponse;
import com.example.windsurfers_weather.entity.Location;
import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WeatherService {

    private static final double MIN_ACCEPTABLE_WIND_SPEED = 5.0;
    private static final double MAX_ACCEPTABLE_WIND_SPEED = 18.0;

    private static final double MIN_ACCEPTABLE_TEMPERATURE = 5.0;
    private static final double MAX_ACCEPTABLE_TEMPERATURE = 35.0;

    private static final int WIND_WEIGHT = 3;

    private final DecimalFormat decimalFormat;
    private final WeatherbitClient weatherbitClient;

    public WeatherService(WeatherbitClient weatherbitClient, DecimalFormat decimalFormat) {
        this.weatherbitClient = weatherbitClient;
        this.decimalFormat = decimalFormat;
    }

    @Getter
    private final List<Location> locations = List.of(
            new Location("Jastarnia", "Poland", 54.7084, 18.6554),
            new Location("Bridgetown", "Barbados", 13.1035, -59.6032),
            new Location("Fortaleza", "Brazil", -3.7318, -38.5266),
            new Location("Pissouri", "Cyprus", 34.6694, 32.7013),
            new Location("Le Morne", "Mauritius", -20.4563, 57.3081)
    );

    public Optional<LocationWeatherResult> getBestLocation(LocalDate date) {

        List<Optional<LocationWeatherResult>> evaluated = locations.stream()
                .map(location -> evaluateLocation(location, date))
                .toList();

        List<LocationWeatherResult> successful = evaluated.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (successful.isEmpty()) {
            log.info("No location is suitable on {}", date);
            return Optional.empty();
        }

        return successful.stream()
                .max(Comparator.comparingDouble(LocationWeatherResult::getScore));
    }

    private Optional<LocationWeatherResult> evaluateLocation(Location location, LocalDate date) {
        WeatherbitResponse response;

        try {
            response = weatherbitClient.getForecast(location.getLat(), location.getLon());
        } catch (WeatherDataUnavailableException ex) {
            log.warn("Skipping location {} due to error: {}", location.getName(), ex.getReason());
            return Optional.empty();
        }

        WeatherData dataForDate = extractWeatherData(response, date);

        if (dataForDate == null) {
            log.warn("No data for location {} on date {}", location.getName(), date);
            return Optional.empty();
        }

        if (isWeatherSuitable(dataForDate)) {
            double score = calculateScore(dataForDate);
            log.info("Calculated score for location={}, in the day={}, score={}", location.getName(), date, score);
            return Optional.of(new LocationWeatherResult(location, dataForDate, score));
        }

        log.info("Location {} not suitable on {}. Temp={}, WindSpeed={}",
                location.getName(), date,
                dataForDate.getTemperature(),
                dataForDate.getWindSpeed());

        return Optional.empty();
    }

    private WeatherData extractWeatherData(WeatherbitResponse response, LocalDate date) {
        return response.getForecastDayList().stream()
                .filter(day -> day.getDatetime().equals(date))
                .findFirst()
                .map(day -> new WeatherData(
                        formatDouble(day.getTemp()),
                        formatDouble(day.getWind_spd()),
                        formatDouble(day.getWind_dir())))
                .orElse(null);
    }

    private double formatDouble(double value) {
        return Double.parseDouble(decimalFormat.format(value));
    }

    private boolean isWeatherSuitable(WeatherData data) {
        return data.getWindSpeed() > MIN_ACCEPTABLE_WIND_SPEED && data.getWindSpeed() < MAX_ACCEPTABLE_WIND_SPEED
                && data.getTemperature() > MIN_ACCEPTABLE_TEMPERATURE && data.getTemperature() < MAX_ACCEPTABLE_TEMPERATURE;
    }

    private double calculateScore(WeatherData data) {
        return data.getWindSpeed() * WIND_WEIGHT + data.getTemperature();
    }


}
