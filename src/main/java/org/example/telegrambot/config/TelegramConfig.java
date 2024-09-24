package org.example.telegrambot.config;

import lombok.RequiredArgsConstructor;
import org.example.telegrambot.bot.TelegramBot;
import org.example.telegrambot.service.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class TelegramConfig {
    private final WeatherService service;
    @Bean
    public TelegramBot testBot(@Value("${bot.name}") String botName,
                               @Value("${bot.token}") String botToken) {
        TelegramBot telegramBot = new TelegramBot(botName, botToken,service);
        try {
            TelegramBotsApi telegramBotsApi =
                    new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return telegramBot;
    }
}
