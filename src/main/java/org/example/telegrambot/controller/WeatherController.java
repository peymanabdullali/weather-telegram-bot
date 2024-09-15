package org.example.telegrambot.controller;

import lombok.RequiredArgsConstructor;
import org.example.telegrambot.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/weather")
@RequiredArgsConstructor
@RestController
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/fetch")
    public String fetchWeather() {
      return   weatherService.fetchWeather();
    }
}