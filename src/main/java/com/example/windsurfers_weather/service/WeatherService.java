package com.example.windsurfers_weather.service;

import com.example.windsurfers_weather.client.WeatherbitClient;
import com.example.windsurfers_weather.dto.LocationWeatherResult;
import com.example.windsurfers_weather.dto.WeatherData;
import com.example.windsurfers_weather.dto.WeatherbitResponse;
import com.example.windsurfers_weather.entity.Location;
import com.example.windsurfers_weather.exception.WeatherDataUnavailableException;
import com.example.windsurfers_weather.utility.WeatherErrorReason;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    private DecimalFormat decimalFormat = new DecimalFormat("##.#", new DecimalFormatSymbols());

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final WeatherbitClient weatherbitClient;

    public WeatherService(WeatherbitClient weatherbitClient) {
        this.weatherbitClient = weatherbitClient;
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

        return locations.stream()
                .map(location -> {
                    try {
                        WeatherbitResponse response = weatherbitClient.getForecast(location.getLat(), location.getLon());

                        WeatherData dataForDate = response.getForecastDayList().stream()
                                .filter(day -> day.getDatetime().equals(date))
                                .findFirst()
                                .map(day -> new WeatherData(Double.parseDouble(decimalFormat.format(day.getTemp())),
                                        Double.parseDouble(decimalFormat.format(day.getWind_spd())),
                                        Double.parseDouble(decimalFormat.format(day.getWind_dir()))))
                                .orElse(null);

                        if (dataForDate == null) {
                            throw new WeatherDataUnavailableException(date, location.getLat(), location.getLon(), WeatherErrorReason.NO_DATA_FOR_DATE);
                        }

                        if (isWeatherSuitable(dataForDate)) {
                            double score = calculateScore(dataForDate);
                            return new LocationWeatherResult(location, dataForDate, score);
                        }
                    } catch (WeatherDataUnavailableException e) {
                        logger.warn("Skipping location {} due to error: {}", location.getName(), e.getReason());
                    }
                    return null;
                })
                .filter(result -> result != null)
                .max(Comparator.comparingDouble(LocationWeatherResult::getScore));
    }

    private boolean isWeatherSuitable(WeatherData data) {
        return data.getWindSpeed() > 5 && data.getWindSpeed() < 18
                && data.getTemperature() > 5 && data.getTemperature() < 35;
    }

    private double calculateScore(WeatherData data) {
        return data.getWindSpeed() * 3 + data.getTemperature();
    }


}
