package com.braille.test_cases;
import com.braille.api.WeatherForecast;
import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherForecastApiTests {
    public static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    public static final List<String> VALID_SUMMARIES = Arrays.asList(
            "Undefined", "Freezing", "Bracing", "Chilly", "Cool", "Mild", "Warm", "Balmy", "Hot", "Sweltering", "Scorching");

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:8081";
    }

    @Test
    @Tag("WeatherForecast")
    @DisplayName("Testing GET /weatherForecast endpoint")
    public void getAllWeatherForecastsApiTest() {

        Response response = RestAssured.get("/weatherforecast");

        assertEquals(200, response.getStatusCode(), "Expected status code 200, but found: " + response.getStatusCode());

        WeatherForecast[] forecasts = response.getBody().as(WeatherForecast[].class);

        assertTrue(forecasts.length > 0, "Expected non-empty list of weather forecasts.");
        assertTrue(DATE_PATTERN.matcher(forecasts[0].date).matches(), "Expected date format YYYY-MM-DD for weather forecast.");
        assertTrue(forecasts[0].temperatureC instanceof Integer, "Expected temperatureC to be of type Integer for weather forecast.");
        assertTrue(VALID_SUMMARIES.contains(forecasts[0].summary), "Expected valid summary for weather forecast.");

        for (WeatherForecast forecast : forecasts) {
            assertNotNull(forecast.id, "Expected non-null ID for weather forecast.");
            assertNotNull(forecast.date, "Expected non-null Date for weather forecast.");
            assertNotNull(forecast.temperatureC, "Expected non-null TemperatureC for weather forecast.");
            assertNotNull(forecast.temperatureF, "Expected non-null TemperatureF for weather forecast.");
            assertNotNull(forecast.summary, "Expected non-null Summary for weather forecast.");
        }
    }

    @Test
    @Tag("WeatherForecast")
    @DisplayName("Testing GET /weatherForecast/{id} endpoint")

    public void getSpecificWeatherForecastByIdApiTest() {
        int id = 1; // Example ID

        Response response = RestAssured.get("/weatherforecast/" + id);

        assertEquals(response.getStatusCode(), 200, "Expected status code 200, but found: " + response.getStatusCode());

        WeatherForecast forecast = response.getBody().as(WeatherForecast.class);

        assertNotNull(forecast, "Expected a weather forecast object in the response but found null.");
        assertNotNull(forecast.id, "Expected non-null ID for weather forecast.");
        assertNotNull(forecast.date, "Expected non-null Date for weather forecast.");
        assertNotNull(forecast.temperatureC, "Expected non-null TemperatureC for weather forecast.");
        assertNotNull(forecast.temperatureF, "Expected non-null TemperatureF for weather forecast.");
        assertNotNull(forecast.summary, "Expected non-null Summary for weather forecast.");
    }

    @Test
    @Tag("WeatherForecast")
    @DisplayName("Testing GET /weatherForecast/{id} endpoint with Invalid Id")

    public void getNonExistingWeatherForecastByIdTest() {
        int nonExistingId = 999; // Example invalid ID

        Response response = RestAssured.get("/weatherforecast/" + nonExistingId);

        assertEquals(response.getStatusCode(), 404, "Expected status code 404, but found: " + response.getStatusCode());
    }

    /**
     * This test case verifies that a user can create a new weather forecast by making a POST request to the "/weatherforecast" endpoint.
     */
    @Test
    @Tag("WeatherForecast")
    @DisplayName("Testing POST /weatherForecast/{id} endpoint")

    public void UserCanCreateWeatherForecastApiTest() {
        WeatherForecast newForecast = new WeatherForecast(1, "2024-07-25", 25, "Warm");

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(newForecast)
                .post("/weatherforecast");

        assertEquals(response.getStatusCode(), 201, "Expected status code 201, but found: " + response.getStatusCode());

        WeatherForecast createdForecast = response.getBody().as(WeatherForecast.class);

        assertNotNull(createdForecast, "Expected a weather forecast object in the response but found null.");
        assertNotNull(createdForecast.date, "Expected non-null Date for the created weather forecast.");
        assertNotNull(createdForecast.temperatureC, "Expected non-null TemperatureC for the created weather forecast.");
        assertNotNull(createdForecast.temperatureF, "Expected non-null TemperatureF for the created weather forecast.");
        assertNotNull(createdForecast.summary, "Expected non-null Summary for the created weather forecast.");
        assertEquals(newForecast.date, createdForecast.date, "Date in response does not match the request.");
        assertEquals(newForecast.temperatureC, createdForecast.temperatureC, "TemperatureC in response does not match the request.");
        assertEquals(newForecast.summary, createdForecast.summary, "Summary in response does not match the request.");

        assertTrue(VALID_SUMMARIES.contains(createdForecast.summary),
                "Summary in response is not a valid value. Expected one of: " + VALID_SUMMARIES + " but found: " + createdForecast.summary);
    }
}

