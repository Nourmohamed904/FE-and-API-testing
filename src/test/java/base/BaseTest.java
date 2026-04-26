package base;

import com.framework.utils.AllureHelper;
import com.framework.utils.ConfigReader;
import com.framework.utils.WebDriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
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

        // Use Allure.addAttachment instead
        Allure.addAttachment("Browser Started", "Navigated to: " + configReader.getProperty("base.url"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            // Take screenshot on failure
            AllureHelper.takeScreenshot(driver);
            AllureHelper.attachPageSource(driver);
            Allure.addAttachment("Test Failed", result.getThrowable().getMessage());
        }

        if (driver != null) {
            WebDriverFactory.quitDriver();
        }
    }
}