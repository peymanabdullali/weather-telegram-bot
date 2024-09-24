package org.example.telegrambot.client;

import org.example.telegrambot.model.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weatherClient", url = "https://api.openweathermap.org/data/2.5/forecast")
public interface WeatherClient {
    @GetMapping
    WeatherResponse getWeatherForCityName(
            @RequestParam("q") String city,
            @RequestParam("appid") String apiKey,
            @RequestParam("lang") String lang
    );

    @GetMapping
    WeatherResponse getWeatherForCoordinate(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("appid") String apiKey,
            @RequestParam("lang") String lang
    );
}
