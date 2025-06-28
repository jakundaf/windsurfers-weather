package com.example.windsurfers_weather.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
public class Location {

    private final String name;
    private final String country;
    private final double lat;
    private final double lon;

}
