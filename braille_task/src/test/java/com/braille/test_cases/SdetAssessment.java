package com.braille.test_cases;

import com.braille.pages.CounterPage;
import com.braille.pages.HomePage;
import com.braille.pages.WeatherPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SdetAssessment {
    private WebDriver driver;
    private WebDriverWait wait;
    private HomePage homePage;
    private WeatherPage weatherPage;
    private CounterPage counterPage;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8080");
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        homePage = new HomePage(driver, wait);
        homePage.waitForPageToLoad();
        weatherPage = new WeatherPage(driver, wait);
        counterPage = new CounterPage(driver, wait);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Tag("UI")
    @DisplayName("User can access all navigation options")
    public void VerifyUserCanAccessAllNavigationOptions() {
        assertEquals(3, homePage.getMENU_ITEMS().size(), "User should see 3 navigation options");
    }

    @Test
    @Tag("UI")
    @DisplayName("User sees correct home page content")
    public void VerifyHomePageContentIsCorrect() {
        assertEquals("Hello, world!", homePage.getHomeHeaderText());
        assertTrue(homePage.isWelcomeTextDisplayed(), "Welcome text should be visible to the user");
        assertTrue(homePage.getABOUT_LINK().isDisplayed(), "About link should be accessible to the user");
    }

    @Test
    @Tag("UI")
    @DisplayName("User can access correct About link")
    public void VerifyAboutLinkIsCorrect() {
        assertTrue(homePage.getABOUT_LINK().isDisplayed());
        assertEquals("About", homePage.getABOUT_LINK().getText());
        assertEquals("https://learn.microsoft.com/aspnet/core/", homePage.getABOUT_LINK().getAttribute("href"));
    }

    @Test
    @Tag("UI")
    @DisplayName("User can increment counter count")
    public void VerifyCounterButtonFunctionality() {
        homePage.navigateToCounter();
        assertEquals("Counter", counterPage.getCounterHeaderText(), "Counter header text does not match expected value");
        assertEquals("Current count: 0", counterPage.getCOUNTER_VALUE(), "Counter value does not match expected initial value");
        counterPage.clickIncrementButton();
        assertEquals("Current count: 1", counterPage.getCOUNTER_VALUE(), "Counter value does not match expected value after increment");
    }

    @Test
    @Tag("UI")
    @DisplayName("User sees correct weather page elements")
    public void VerifyWeatherPageContainsCorrectElements() {
        homePage.navigateToWeather();
        assertEquals("http://localhost:8080/weather", driver.getCurrentUrl(), "Current URL does not contain weather");
        assertEquals("Weather", weatherPage.getWeatherHeaderText(), "Header does not contain 'Weather'");
        assertTrue(weatherPage.isDescriptionTextDisplayed(), "Description text is not displayed");
        assertTrue(weatherPage.isDownloadButtonDisplayed(), "Download Forecast Data button is not displayed");
        assertTrue(weatherPage.isFileInputDisplayed(), "File input is not displayed");
    }

    @Test
    @Tag("UI")
    @DisplayName("User sees correct weather forecast table structure")
    public void VerifyWeatherForecastTableStructure() {
        homePage.navigateToWeather();
        assertTrue(weatherPage.getWEATHER_TABLE().isDisplayed(), "Weather table is not displayed");
        List<WebElement> tableRows = weatherPage.getWeatherTableRows();
        assertEquals(5, tableRows.size(), "Weather forecast table should have 5 data rows");
        for (WebElement row : tableRows) {
            List<WebElement> cells = weatherPage.getWeatherTableCells(row);
            assertTrue(cells.get(0).getText().matches("\\d{2}/\\d{2}/\\d{4}"), "Date should be in MM/DD/YYYY format");
            assertTrue(cells.get(1).getText().matches("-?\\d+"), "Temperature (C) should be a number");
            assertTrue(cells.get(2).getText().matches("-?\\d+"), "Temperature (F) should be a number");
            assertFalse(cells.get(3).getText().isEmpty(), "Summary should not be empty");
        }
    }

    @Test
    @Tag("UI")
    @DisplayName("User sees refreshed weather forecast data")
    public void VerifyWeatherForecastDataRefresh() {
        homePage.navigateToWeather();
        List<String> initialData = weatherPage.captureTableData();
        driver.navigate().refresh();
        wait.until(ExpectedConditions.presenceOfElementLocated(weatherPage.WEATHER_TABLE));
        List<String> refreshedData = weatherPage.captureTableData();
        wait.until(ExpectedConditions.presenceOfElementLocated(weatherPage.WEATHER_TABLE));
        assertNotEquals(initialData, refreshedData, "Weather forecast data should change after refresh");
    }

    @Test
    @Tag("UI")
    @DisplayName("User can upload weather data file")
    public void VerifyFileUploadFunctionality() {
        homePage.navigateToWeather();
        String fileName = "test_weather_data.csv";
        Path testFilePath = createTestFile(fileName);
        weatherPage.uploadFile(testFilePath.toAbsolutePath().toString());
        wait.until(ExpectedConditions.visibilityOfElementLocated(weatherPage.WEATHER_TABLE));
        List<WebElement> rows = weatherPage.getWeatherTableRows();
        assertFalse(rows.isEmpty(), "Table should be populated with data after file upload");
    }

    @Test
    @Tag("UI")
    @DisplayName("User can interact with Download Forecast Data button")
    public void verifyDownloadForecastDataButtonFunctionality() {
        homePage.navigateToWeather();
        wait.until(ExpectedConditions.visibilityOfElementLocated(weatherPage.WEATHER_TABLE));
        weatherPage.clickDownloadButton();
        assertTrue(weatherPage.isDownloadButtonDisplayed(), "Download button should still be displayed after click");
        assertTrue(weatherPage.isDownloadButtonEnabled(), "The Download Forecast Data button should be enabled");
        assertTrue(weatherPage.getErrorMessages().isEmpty(), "No new error messages or popups should appear");
    }

    /**
     * Creates a test file with the specified name in the temporary directory.
     * The file contains weather forecast data in CSV format.
     *
     * @param fileName The name of the test file to be created.
     * @return The {@link Path} of the created test file.
     * @throws RuntimeException If an error occurs while creating the test file.
     */
    private Path createTestFile(String fileName) {
        try {
            Path testFilePath = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
            String content = "Date,Temp. (C),Temp. (F),Summary\n" +
                    "07/28/2024,25,77,Sunny\n" +
                    "07/29/2024,30,86,Hot\n" +
                    "07/30/2024,20,68,Mild\n";
            Files.write(testFilePath, content.getBytes());
            return testFilePath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test file", e);
        }
    }
}