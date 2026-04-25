package com.framework.base;

import com.framework.utils.ConfigReader;
import com.framework.utils.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WebDriver driver;
    protected ConfigReader configReader;

    @BeforeMethod
    public void setUp() {
        configReader = new ConfigReader();
        driver = WebDriverFactory.getDriver(configReader);
        driver.manage().window().maximize();
        driver.get(configReader.getProperty("base.url"));
        System.out.println("Browser started and navigated to: " + configReader.getProperty("base.url"));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            WebDriverFactory.quitDriver();
            System.out.println("Browser closed.");
        }
    }
}