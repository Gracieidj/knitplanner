package knitplanner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.*;

/**
 * Service class responsible for fetching current weather data
 * from the OpenWeatherMap API and categorizing it into simple weather categories.
 */
public class WeatherService {

    // Retrieve the OpenWeatherMap API key from environment variables for security
    private static final String API_KEY = System.getenv("OPENWEATHER_API_KEY");

    /**
     * Fetches the current weather for the specified city and returns a
     * categorized weather description string.
     *
     * @param city The name of the city for which to fetch weather data.
     * @return A weather category string like "Cold", "Warm and Clear", or fallback "All".
     */
    public String getCurrentWeatherCategory(String city) {
        // Check if API key is set; if not, log error and fallback to "All"
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.err.println("Error: OPENWEATHER_API_KEY environment variable is not set.");
            return "All"; // fallback category if no API key
        }

        // Validate that city name is provided; else log error and fallback
        if (city == null || city.isBlank()) {
            System.err.println("Error: City name is null or empty.");
            return "All"; // fallback category if invalid city input
        }

        try {
            // Construct the API URL with HTTPS for security, using metric units for temperature
            String urlStr = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
                city.trim(), API_KEY);

            URI uri = URI.create(urlStr);

            // Create a new HTTP client to send the request
            HttpClient client = HttpClient.newHttpClient();

            // Build the HTTP GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            // Send the request synchronously and receive the response as a String
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if HTTP response status is 200 OK; if not, log error and fallback
            if (response.statusCode() != 200) {
                System.err.printf("Error: HTTP %d from weather API for city '%s'%n", response.statusCode(), city);
                return "All"; // fallback category on bad response
            }

            // Parse the JSON response string into a JsonObject using Gson
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            // Validate that the expected fields exist in the JSON response
            if (!json.has("main") || !json.has("weather")) {
                System.err.println("Error: Unexpected API response structure.");
                return "All"; // fallback if response format is unexpected
            }

            // Extract the temperature value (in Celsius) from the "main" JSON object
            double temp = json.getAsJsonObject("main").get("temp").getAsDouble();

            // Extract the main weather condition description from the first element of "weather" array
            String weatherMain = json.getAsJsonArray("weather")
                .get(0).getAsJsonObject()
                .get("main").getAsString();

            // Categorize weather based on temperature and condition:
            if (temp < 15) {
                // If temperature is cold (<15°C), check for wet conditions like snow or rain
                if ("Snow".equalsIgnoreCase(weatherMain) || "Rain".equalsIgnoreCase(weatherMain)) {
                    return "Cold and Wet";
                }
                // Otherwise just cold
                return "Cold";
            } else {
                // For warmer temperatures (>=15°C), categorize based on conditions
                if ("Clear".equalsIgnoreCase(weatherMain)) {
                    return "Warm and Clear";
                } else if ("Rain".equalsIgnoreCase(weatherMain)) {
                    return "Warm and Rainy";
                }
                // Default warm category if no other conditions matched
                return "Warm";
            }

        } catch (Exception e) {
            // Catch any exception during HTTP call or parsing and log it
            System.err.println("Exception while fetching weather:");
            e.printStackTrace();

            // Return fallback category to avoid crashing the app
            return "All";
        }
    }
}
