package com.example.weather_app;

public class ForecastItem {
    String date;
    String condition;
    double maxTemp;

    public ForecastItem(String date, String condition, double maxTemp) {
        this.date = date;
        this.condition = condition;
        this.maxTemp = maxTemp;
    }
}
