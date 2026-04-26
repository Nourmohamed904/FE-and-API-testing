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
            // Use default credentials as fallback
            validEmail = "john.doe@example.com";
            validPassword = "Password123";
        }
    }

    @Test
    public void testChangeCurrency() {
        HomePage home = new HomePage(driver);

        // Login with credentials from Excel
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(validEmail, validPassword);

        // Go to Desktops
        SearchPage searchPage = new SearchPage(driver);
        searchPage.goToDesktops();

        // Get prices in USD (default)
        List<String> usdPrices = searchPage.getAllProductPrices();
        Allure.addAttachment("USD Prices", usdPrices.toString());

        // Change currency to Euro
        home.header().changeCurrencyToEuro();

        // Get prices in Euro
        List<String> euroPrices = searchPage.getAllProductPrices();
        Allure.addAttachment("Euro Prices", euroPrices.toString());

        // Verify prices changed
        Assert.assertNotEquals(usdPrices, euroPrices,
                "Prices should change after currency update");

        // Logout
        home.header().logout();
    }
}