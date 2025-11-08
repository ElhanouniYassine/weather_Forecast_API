package com.example.demoForGit.controllers;

import com.example.demoForGit.models.WeatherData;
import com.example.demoForGit.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;
    @GetMapping("/{city}")
    public WeatherData getWeather(@PathVariable String city){
        return weatherService.getWeather(city);
    }
}
