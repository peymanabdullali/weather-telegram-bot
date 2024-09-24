package org.example.telegrambot.model;

import lombok.Data;

import java.util.List;

@Data
public class WeatherResponse {

    private List<WeatherList> list;
    private City city;

    @Data
    public static class WeatherList {
        private Main main;
        private List<Weather> weather;
        private Wind wind;
        private String dt_txt;
    }

    @Data
    public static class Main {
        private double temp;
        private double feels_like;
        private int humidity;
    }

    @Data
    public static class Weather {
        private String main;
        private String description;
    }

    @Data
    public static class Wind {
        private double speed;
    }

    @Data
    public static class City {
        private String name;
    }

}
