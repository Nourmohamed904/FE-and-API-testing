package base;

import com.framework.utils.AllureHelper;
import com.framework.utils.ConfigReader;
import com.framework.utils.WebDriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WebDriver driver;
    protected ConfigReader configReader;
    protected TestDataSupport testDataSupport;

    @BeforeMethod
    public void setUp() {
        configReader = getConfigReader();
        testDataSupport = getTestDataSupport();
        driver = WebDriverFactory.getDriver(configReader);
        driver.manage().window().maximize();
        driver.get(configReader.getRequiredProperty("base.url"));

        Allure.addAttachment("Browser Started", "Navigated to: " + configReader.getRequiredProperty("base.url"));
    }

    protected ConfigReader getConfigReader() {
        if (configReader == null) {
            configReader = new ConfigReader();
        }
        return configReader;
    }

    protected TestDataSupport getTestDataSupport() {
        if (testDataSupport == null) {
            testDataSupport = new TestDataSupport(getConfigReader());
        }
        return testDataSupport;
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                AllureHelper.takeScreenshot(driver);
                AllureHelper.attachPageSource(driver);
            } catch (WebDriverException ignored) {
            }

            if (result.getThrowable() != null) {
                Allure.addAttachment("Test Failed", result.getThrowable().getMessage());
            }
        }

        if (driver != null) {
            WebDriverFactory.quitDriver();
        }
    }
}
