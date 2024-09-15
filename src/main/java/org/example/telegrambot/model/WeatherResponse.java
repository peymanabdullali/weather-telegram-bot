package org.example.telegrambot.model;

import lombok.Data;
import org.example.telegrambot.model.*;

import java.util.List;

@Data
public class WeatherResponse {

    private Coord coord;
    private List<Weather> weather;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Rain rain;
    private Sys sys;
    private String base;
    private int visibility;
    private int dt;
    private String name;
    private int timezone;
    private int id;
    private int cod;

}
