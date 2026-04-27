package tests;

import base.BaseTest;
import com.framework.pages.*;
import com.framework.utils.ExcelReader;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;

public class CurrencyTest extends BaseTest {

    private String validEmail;
    private String validPassword;

    @BeforeMethod
    public void getValidCredentials() {
        try {
            ExcelReader reader = new ExcelReader("testdata/testdata.xlsx", "Login");
            Object[][] data = reader.getData();
            for (Object[] row : data) {
                if (row[2].toString().equalsIgnoreCase("valid")) {
                    validEmail = row[0].toString();
                    validPassword = row[1].toString();
                    break;
                }
            }
        } catch (Exception e) {
            validEmail = "demo@tutorialsninja.com";
            validPassword = "demo";
        }
        Allure.addAttachment("Credentials", "Using: " + validEmail);
    }

    @Test
    public void testChangeCurrency() {
        HomePage home = new HomePage(driver);

        // Login with credentials from Excel
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(validEmail, validPassword);

        // Use SearchPage but stay on home page by searching for something common
        // Or just work with home page products
        SearchPage searchPage = new SearchPage(driver);

        // Get prices in USD (default) - using home page featured products
        List<String> usdPrices = searchPage.getAllProductPrices();
        Allure.addAttachment("USD Prices", usdPrices != null ? usdPrices.toString() : "No products found");

        // Change currency to Euro
        home.header().changeCurrencyToEuro();

        // Wait for page to refresh with new currency
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Get prices in Euro
        List<String> euroPrices = searchPage.getAllProductPrices();
        Allure.addAttachment("Euro Prices", euroPrices != null ? euroPrices.toString() : "No products found");

        // Verify prices changed (if products exist)
        if (usdPrices != null && euroPrices != null && !usdPrices.isEmpty() && !euroPrices.isEmpty()) {
            Assert.assertNotEquals(usdPrices, euroPrices,
                    "Prices should change after currency update");
        } else {
            Allure.addAttachment("Warning", "No products found to compare prices");
            // Force a simple currency symbol check on the page
            String pageSource = driver.getPageSource();
            boolean currencyChanged = pageSource.contains("€") || pageSource.contains("Euro");
            Assert.assertTrue(currencyChanged, "Currency symbol should change to Euro");
        }

        // Logout
        home.header().logout();
    }
}