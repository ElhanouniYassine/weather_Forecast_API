package com.example.demoForGit.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
public class WeatherData {
    @Id
    @GeneratedValue
    private Long id;
    private String city;
    private String description;
    private double temperature;
    private double humidity;
    private double windSpeed;
    private LocalDateTime date;

    public String getCity() {
        return city;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

// other entites in futur: FavoriteCity , WeatherForecast , User , WeatherConversion
