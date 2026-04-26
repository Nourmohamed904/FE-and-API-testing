package tests;

import base.BaseTest;
import com.framework.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class CurrencyTest extends BaseTest {

    @Test
    public void testChangeCurrency() {
        HomePage home = new HomePage(driver);

        // Login first
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid("test@email.com", "Password123");

        // Go to Desktops
        SearchPage searchPage = new SearchPage(driver);
        searchPage.goToDesktops();

        // Get prices in USD (default)
        List<String> usdPrices = searchPage.getAllProductPrices();

        // Change currency to Euro
        home.header().changeCurrencyToEuro();

        // Get prices in Euro
        List<String> euroPrices = searchPage.getAllProductPrices();

        // Verify prices changed
        Assert.assertNotEquals(usdPrices, euroPrices, "Prices should change after currency update");

        // Logout
        home.header().logout();
    }
}