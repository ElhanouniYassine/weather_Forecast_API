package com.example.demoForGit.services;

import com.example.demoForGit.models.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apikey;

    private final String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q={city}&appid={apiKey}&units=metric";
    private final String forecastUrl = "http://api.openweathermap.org/data/2.5/forecast?q={city}&appid={apiKey}&units=metric";
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    // Get current weather data for a given city
    // Get current weather data for a given city
    public WeatherData getWeather(String city) {
        String url = apiUrl;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, city, apikey);
            List<WeatherData> weatherDataList = parseWeatherData(response.getBody());

            if (weatherDataList.isEmpty()) {
                throw new RuntimeException("No weather data found for city: " + city);
            }
            return weatherDataList.get(0); // Assuming only one weather data for current weather
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching weather data: " + e.getMessage());
            throw new RuntimeException("Error fetching weather data: " + e.getMessage(), e);
        }
    }


    // Get weather forecast data for a given city
    public List<WeatherData> getWeatherForecast(String city) {
        String url = forecastUrl;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, city, apikey);
            logger.debug("API Response: " + response.getBody()); // Debug log of the API response (without sensitive data)

            return parseWeatherData(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching forecast data: " + e.getMessage());
            throw new RuntimeException("Error fetching forecast data: " + e.getMessage(), e);
        }
    }

    // Parse JSON response into WeatherData object
    private List<WeatherData> parseWeatherData(String jsonResponse) {
        try {
            // Use Jackson's ObjectMapper to parse the JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            List<WeatherData> weatherDataList = new ArrayList<>();

            // Extract the list of forecasts from the "list" node
            JsonNode listNode = rootNode.path("list");

            if (listNode.isArray()) {
                // Loop through each forecast entry
                for (JsonNode forecastNode : listNode) {
                    WeatherData weatherData = new WeatherData();

                    // Extracting city name (from the "city" object)
                    JsonNode cityNode = rootNode.path("city");
                    weatherData.setCity(cityNode.path("name").asText("Unknown City"));

                    // Extracting temperature
                    JsonNode mainNode = forecastNode.path("main");
                    weatherData.setTemperature(mainNode.path("temp").asDouble(0.0));

                    // Extracting weather description
                    JsonNode weatherNode = forecastNode.path("weather").get(0);
                    weatherData.setDescription(weatherNode != null && weatherNode.has("description") ?
                            weatherNode.path("description").asText() : "No description available");

                    // Extracting humidity
                    weatherData.setHumidity(mainNode.path("humidity").asDouble(0.0));

                    // Extracting wind speed
                    JsonNode windNode = forecastNode.path("wind");
                    weatherData.setWindSpeed(windNode.path("speed").asDouble(0.0));

                    // Extracting forecast date/time
                    String forecastDateTime = forecastNode.path("dt_txt").asText();
                    weatherData.setDate(java.time.LocalDateTime.parse(forecastDateTime.replace(" ", "T")));

                    // Add the parsed weather data to the list
                    weatherDataList.add(weatherData);
                }
            }

            return weatherDataList;

        } catch (Exception e) {
            logger.error("Error parsing weather data: " + e.getMessage());
            throw new RuntimeException("Error parsing weather data: " + e.getMessage(), e);
        }
    }
}
