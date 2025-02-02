package com.braille.utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Driver {

    private Driver() {
    }

    private static WebDriver driver;

    public static WebDriver getDriver() {

        if (driver == null) {
            String browserType = ConfigurationReader.getProperty("browser");
            switch (browserType) {
                case "chrome":
                    driver = new ChromeDriver();
                    driver.manage().window().maximize();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    driver.manage().window().maximize();
                    break;
            }
        }
        return driver;
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
