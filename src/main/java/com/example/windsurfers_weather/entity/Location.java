package com.example.windsurfers_weather.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private String name;
    private String country;
    private double lat;
    private double lon;

}
