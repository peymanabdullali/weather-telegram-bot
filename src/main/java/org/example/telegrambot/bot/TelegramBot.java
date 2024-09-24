package org.example.telegrambot.bot;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.example.telegrambot.enums.Step;
import org.example.telegrambot.enums.WeatherType;
import org.example.telegrambot.model.*;
import org.example.telegrambot.service.WeatherService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final WeatherService service;
    private final String botName;
    private final Map<Long, String> userCities = new HashMap<>();

    public TelegramBot(String botName, String botToken, WeatherService service) {
        super(botToken);
        this.botName = botName;
        this.service = service;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                nextStep(chatId, message.getText(), null);
            } else if (message.getLocation() != null) {
                WeatherResponse weatherResponse1 = service.fetchWeatherForCoordinate(
                        message.getLocation().getLatitude(), message.getLocation().getLongitude());
                nextStep(chatId, weatherResponse1.getCity().getName(), weatherResponse1);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void nextStep(long chatId, String text, WeatherResponse response) throws TelegramApiException {
        String city = userCities.get(chatId);
        switch (text) {
            case "/start":
                sendTelegramMessage(chatId,
                        "Xoş Gəlmisiniz! Zehmet olmasa şəhərin adını və ya konumunuzu daxil edin.", Step.FIRST);
                break;
            case "🌦️ 5 Günlük Hava Proqnozu":
                if (city != null) {
                    try {
                        if (response != null) {
                            weatherMessage(response, chatId, WeatherType.TODAY);
                        }
                        else {
                            WeatherResponse weather2 = service.fetchWeatherForCityName(city);
                            weatherMessage(weather2, chatId, WeatherType.FIVE_DAY);
                        }
                    } catch (FeignException.FeignClientException exception) {
                        sendTelegramMessage(chatId, "Şəhərin adını düzgün daxil edin!", Step.FIRST);
                    }
                } else {
                    sendTelegramMessage(chatId,
                            "Zəhmət olmasa öncə şəhərin adını daxil edin.", Step.FIRST);
                }
                break;
            case "/stop":
                userCities.remove(chatId);
                sendTelegramMessage(chatId,
                        "Botu tərk etdiniz. Şəhər məlumatınız silindi.", Step.FIRST);
                break;
            case "🌡️ Bugünkü Hava":
                if (city != null) {
                    try {
                        if (response != null) {
                            weatherMessage(response, chatId, WeatherType.TODAY);
                        }
                        else {
                            WeatherResponse weather2 = service.fetchWeatherForCityName(city);
                            weatherMessage(weather2, chatId, WeatherType.TODAY);
                        }
                    } catch (FeignException.FeignClientException exception) {
                        sendTelegramMessage(chatId, "Şəhərin adını düzgün daxil edin!", Step.FIRST);
                    }
                } else {
                    sendTelegramMessage(chatId,
                            "Zəhmət olmasa öncə şəhərin adını daxil edin.", Step.FIRST);
                }
                break;
            default:
                userCities.put(chatId, text);
                sendTelegramMessage(chatId, "Seçim edin:", Step.SECOND);
                break;
        }
    }

    private void weatherMessage(WeatherResponse weatherResponse, Long chatId, WeatherType type) {
        try {
            byte a = 0;
            WeatherResponse.City city = weatherResponse.getCity();
            StringBuilder responseMessage = new StringBuilder();
            responseMessage.append(String.format("📍 Şəhər:      <b>%s</b>\n\n", city.getName()));
            String date = weatherResponse.getList().get(0).getDt_txt().substring(0, 10);
            for (WeatherResponse.WeatherList weatherList : weatherResponse.getList()) {
                a++;
                String weatherInfo = service.getString(weatherList);
                if (!date.equals(weatherList.getDt_txt().substring(0, 10))) {
                    responseMessage.insert(0, String.format("📅 Tarix:     <b>%s</b>\n\n", date));
                    sendTelegramMessage(chatId, responseMessage.toString(), Step.SECOND);
                    responseMessage.setLength(0);
                    date = weatherList.getDt_txt().substring(0, 10);
                    if (type == WeatherType.TODAY) {
                        sendTelegramMessage(chatId, "Şəhərin adını daxil edin.", Step.SECOND);
                        break;
                    }
                }
                if (weatherResponse.getList().size() - 1 == a) {
                    sendTelegramMessage(chatId, "Şəhərin adını daxil edin.", Step.SECOND);
                }
                responseMessage.append(weatherInfo);
            }
        } catch (TelegramApiException e) {
            log.error("Mesaj gonderilende xeta yarandi: {}", e.getMessage());
        }
    }

    private void sendTelegramMessage(Long chatId, String message, Step step) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(chatId);
        if (message != null) {
            sendMessage.setText(message);
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        if (step.equals(Step.FIRST)) {
            replyKeyboardMarkup.setKeyboard(new ArrayList<>());
        } else if (step.equals(Step.SECOND)) {
            sendMessage.setReplyMarkup(replyKeyboardMarkupSecond(replyKeyboardMarkup));
        }
        execute(sendMessage);
    }

    private ReplyKeyboardMarkup replyKeyboardMarkupSecond(ReplyKeyboardMarkup replyKeyboardMarkup) {
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add(new KeyboardButton("🌦️ 5 Günlük Hava Proqnozu"));
        row1.add(new KeyboardButton("🌡️ Bugünkü Hava"));
        row2.add(new KeyboardButton("/stop"));
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }
}