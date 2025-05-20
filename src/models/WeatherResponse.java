package models;

import java.util.List;

/**
 * Represents the JSON response structure from the OpenWeatherMap API
 * for current weather data, suitable for Gson deserialization.
 */
public class WeatherResponse {
    // List of weather conditions (usually contains one or more weather descriptions)
    private List<Weather> weather;

    // Main weather data like temperature, pressure, humidity
    private Main main;

    // Name of the city for which the weather data was requested
    private String name;

    // Getters for the outer class fields

    /** Returns the list of weather conditions */
    public List<Weather> getWeather() { return weather; }

    /** Returns the main weather data */
    public Main getMain() { return main; }

    /** Returns the city name */
    public String getName() { return name; }

    /**
     * Inner static class representing individual weather conditions,
     * such as "Rain", "Clear", etc., including description.
     */
    public static class Weather {
        // Main weather category (e.g., "Rain", "Clear", "Clouds")
        private String main;

        // Detailed weather description (e.g., "light rain", "scattered clouds")
        private String description;

        /** Returns the main weather category */
        public String getMain() { return main; }

        /** Returns the detailed weather description */
        public String getDescription() { return description; }
    }

    /**
     * Inner static class representing main weather metrics,
     * currently only temperature is included.
     */
    public static class Main {
        // Current temperature in Celsius (if units=metric is specified)
        private double temp;

        /** Returns the temperature */
        public double getTemp() { return temp; }
    }
}
