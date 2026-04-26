package com.framework.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class AllureHelper {

    @Attachment(value = "Screenshot on failure", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            return null;
        }
    }

    @Attachment(value = "Test Log: {0}", type = "text/plain")
    public static String attachLog(String message) {
        return message;
    }

    @Attachment(value = "Page Source", type = "text/html")
    public static String attachPageSource(WebDriver driver) {
        return driver.getPageSource();
    }

    // Alternative method using Allure.addAttachment (more flexible)
    public static void addLog(String message) {
        Allure.addAttachment("Log Message", "text/plain", message);
    }

    public static void addScreenshot(WebDriver driver, String name) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", screenshot, "png");
        } catch (Exception e) {
            Allure.addAttachment("Screenshot Error", "text/plain", e.getMessage());
        }
    }
}