package com.example.demoForGit.services;

import com.example.demoForGit.models.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private String apikey;
    private final String apiUrl="http://api.openweathermap.org/data/2.5/weather?q={city},uk&APPID={apiKey}&units=metric";

    public WeatherData getWeather(String city) {
        String url = apiUrl;
        RestTemplate restTemplate = new RestTemplate();

        // Fetching data from the OpenWeatherMap API
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, city, apikey);

        // Process the response into WeatherData (you'll parse the JSON here)
        return parseWeatherData(response.getBody(), city);
    }

    private WeatherData parseWeatherData(String jsonResponse, String city) {

        WeatherData weatherData = new WeatherData();
        weatherData.setCity(city);
        weatherData.setTemperature(20.0);
        weatherData.setDescription("Clear Sky");
        weatherData.setHumidity(50.0);
        weatherData.setWindSpeed(5.0);
        weatherData.setDate(java.time.LocalDateTime.now());

        return weatherData;
    }
}
