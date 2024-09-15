package org.example.telegrambot.model;

import lombok.Data;

@Data

public class Wind {
    private double speed;
    private int deg;
    private double gust;
}
