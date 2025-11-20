package com.example.demoForGit.controllers;

import com.example.demoForGit.models.WeatherData;
import com.example.demoForGit.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    // Endpoint for current weather of a city
    @GetMapping("/{city}")
    public String getWeather(@PathVariable String city){
        return  weatherService.getWeather(city);
    }

    @GetMapping("/{lat:.+}/{lon:.+}")
    public String getReverseGeocoding(@PathVariable("lat") double lat,@PathVariable("lon") double lon){
        return weatherService.getReverseGeocoding(lat,lon);
    }

    // Endpoint for weather forecast of a city (returns a list of forecasts)
    @GetMapping("/forecast/{city}")
    public List<WeatherData> getWeatherForecast(@PathVariable String city){
        return weatherService.getWeatherForecast(city);
    }
}
