package com.braille.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private final By MENU_ITEMS = By.cssSelector(".nav-item .nav-link");
    private final By HOME_HEADER = By.tagName("h1");
    private final By WELCOME_TEXT = By.xpath("//article[@class='content px-4'][contains(.,'Hello, world!')]");
    private final By ABOUT_LINK = By.cssSelector("a[href='https://learn.microsoft.com/aspnet/core/']");
    private final By WEATHER_LINK = By.cssSelector("a[href='weather']");
    private final By COUNTER_LINK = By.cssSelector("a[href='counter']");

    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public List<WebElement> getMENU_ITEMS() {
        return driver.findElements(MENU_ITEMS);
    }

    public String getHomeHeaderText() {
        return driver.findElement(HOME_HEADER).getText();
    }

    public boolean isWelcomeTextDisplayed() {
        return driver.findElement(WELCOME_TEXT).isDisplayed();
    }

    public WebElement getABOUT_LINK() {
        return driver.findElement(ABOUT_LINK);
    }
    /**
     * Navigates to the Weather page by clicking the "Weather" link in the navigation bar.
     * Waits for the Weather page to load by checking the visibility of the weather link.
     *
     * @return A new instance of the WeatherPage class representing the Weather page.
     */
    public WeatherPage navigateToWeather() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(WEATHER_LINK));
        driver.findElement(WEATHER_LINK).click();
        return new WeatherPage(driver, wait);
    }
    /**
     * Navigates to the Counter page by clicking the "Counter" link in the navigation bar.
     * Waits for the Counter page to load by checking the visibility of the counter link.
     *
     * @return A new instance of the CounterPage class representing the Counter page.
     */
    public CounterPage navigateToCounter() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(COUNTER_LINK));
        driver.findElement(COUNTER_LINK).click();
        return new CounterPage(driver, wait);
    }
    /**
     * Waits for the Home page to load by checking the presence of the navigation menu items.
     * This method should be called after navigating to the Home page to ensure that the page has fully loaded.
     */
    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".nav-item .nav-link")));
    }
}