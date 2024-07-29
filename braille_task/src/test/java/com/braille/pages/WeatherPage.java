package com.braille.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class WeatherPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public final By WEATHER_HEADER = By.xpath("//h1[text()='Weather']");
    private final By DESCRIPTION_TEXT = By.xpath("//p[contains(text(), 'This component demonstrates showing data.')]");
    private final By DOWNLOAD_BUTTON = By.xpath("//button[text()='Download Forecast Data']");
    private final By FILE_INPUT = By.xpath("//input[@type='file']");
    public final By WEATHER_TABLE = By.xpath("//table[@class='table']");

    public WeatherPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public String getWeatherHeaderText() {
        return driver.findElement(WEATHER_HEADER).getText();
    }

    public boolean isDescriptionTextDisplayed() {
        return driver.findElement(DESCRIPTION_TEXT).isDisplayed();
    }

    public boolean isDownloadButtonDisplayed() {
        return driver.findElement(DOWNLOAD_BUTTON).isDisplayed();
    }

    public boolean isFileInputDisplayed() {
        return driver.findElement(FILE_INPUT).isDisplayed();
    }

    public WebElement getWEATHER_TABLE() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(WEATHER_TABLE));
        return driver.findElement(WEATHER_TABLE);
    }

    public void uploadFile(String filePath) {
        driver.findElement(FILE_INPUT).sendKeys(filePath);
    }

    public void clickDownloadButton() {
        driver.findElement(DOWNLOAD_BUTTON).click();
    }

    public List<WebElement> getWeatherTableRows() {
        return driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
    }

    public List<WebElement> getWeatherTableCells(WebElement row) {
        return row.findElements(By.tagName("td"));
    }

    /**
     * Captures the text data from the weather table and returns it as a list of strings.
     * Each string in the list represents a row from the table.
     *
     * @return A list of strings containing the text data from the weather table.
     */
    public List<String> captureTableData() {
        List<String> data = new ArrayList<>();
        List<WebElement> tableRows = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        for (WebElement row : tableRows) {
            data.add(row.getText());
        }
        return data;
    }

    public boolean isDownloadButtonEnabled() {
        return driver.findElement(DOWNLOAD_BUTTON).isEnabled();
    }

    public List<WebElement> getErrorMessages() {
        return driver.findElements(By.xpath("//*[contains(@class, 'error') or contains(@class, 'popup')]"));
    }

}