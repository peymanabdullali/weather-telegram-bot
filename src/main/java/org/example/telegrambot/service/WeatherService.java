package org.example.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.example.telegrambot.client.WeatherClient;
import org.example.telegrambot.model.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClient client;

    public WeatherResponse fetchWeatherForCityName(String city) {
        String apiKey = "d8e0d042fe48a32943fa8f4853995454";
        String lang = "az";
        return client.getWeatherForCityName(city, apiKey,lang);
    }

    public WeatherResponse fetchWeatherForCoordinate(double lat, double lon) {

        String apiKey = "d8e0d042fe48a32943fa8f4853995454";
        String lang = "az";
        return client.getWeatherForCoordinate(lat, lon, apiKey, lang);
    }
    public String getString(WeatherResponse.WeatherList weatherList) {
        WeatherResponse.Main main = weatherList.getMain();
        WeatherResponse.Weather weather = weatherList.getWeather().get(0);
        return String.format(
                "⌚ Saat:        <b>%s</b>\n\n" +
                        "🌡️ Tempratur:        <b>%.2f°C</b>\n\n" +
                        "🤔 Hissedilən:         <b>%.2f°C</b>\n\n" +
                        "🌥️ Durum:   <b>%s (%s)</b>\n\n" +
                        "💧 Rütubət:             <b>%d%%</b>\n\n" +
                        "💨 Küləyin sürəti:   <b>%.2f m/s</b>\n\n" +
                        "--------------------------\n\n",
                weatherList.getDt_txt().substring(11, 19), kelvinToCelsius(main.getTemp()), kelvinToCelsius(main.getFeels_like()), weather.getMain(),
                weather.getDescription(), main.getHumidity(), weatherList.getWind().getSpeed()
        );
    }
    private double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }


}
