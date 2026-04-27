package com.framework.utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class AllureHelper {

    @Attachment(value = "Screenshot on failure", type = "image/png")
    public static void takeScreenshot(WebDriver driver) {
        ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Test Log: {0}", type = "text/plain")
    public static void attachLog(String message) {
    }

    @Attachment(value = "Page Source", type = "text/html")
    public static void attachPageSource(WebDriver driver) {
        driver.getPageSource();
    }
}