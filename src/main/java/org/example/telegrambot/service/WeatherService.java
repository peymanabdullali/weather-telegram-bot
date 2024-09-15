package org.example.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.example.telegrambot.client.WeatherClient;
import org.example.telegrambot.model.WeatherResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClient client;

    public String fetchWeather() {
        double lat = 41.0715;
        double lon = 47.4630;
        String apiKey = "d8e0d042fe48a32943fa8f4853995454";
        String lang = "az";
        WeatherResponse weather = client.getWeather(lat, lon, apiKey, lang);
        return weather.getName();
    }
}
