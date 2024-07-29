package com.braille.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CounterPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private final By COUNTER_HEADER = By.tagName("h1");
    private final By COUNTER_VALUE = By.xpath("//p[@role='status']");
    private final By COUNTER_INCREMENT_BUTTON = By.xpath("//button[text()='Click me']");

    public CounterPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public String getCounterHeaderText() {
        return driver.findElement(COUNTER_HEADER).getText();
    }

    public String getCOUNTER_VALUE() {
        return driver.findElement(COUNTER_VALUE).getText();
    }

    public void clickIncrementButton() {
        driver.findElement(COUNTER_INCREMENT_BUTTON).click();
    }
}