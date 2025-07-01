package com.example.windsurfers_weather.service;

import com.example.windsurfers_weather.entity.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherService {

    private final List<Location> locations = List.of(
            new Location("Jastarnia", "Poland", 54.7084, 18.6554),
            new Location("Bridgetown", "Barbados", 13.1035, -59.6032),
            new Location("Fortaleza", "Brazil", -3.7318, -38.5266),
            new Location("Pissouri", "Cyprus", 34.6694, 32.7013),
            new Location("Le Morne", "Mauritius", -20.4563, 57.3081)
    );

    public List<Location> getLocations() {
        return locations;
    }

}