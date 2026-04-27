package tests;

import base.BaseTest;
import com.framework.pages.AccountPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.SearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CurrencyTest extends BaseTest {

    @Test
    public void testChangeCurrency() {
        String[] credentials = testDataSupport.getValidLoginCredentials();
        HomePage home = new HomePage(driver);

        // Step 1 (Assignment): Login by any valid user.
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(credentials[0], credentials[1]);
        Assert.assertTrue(account.isAccountPageDisplayed(), "Login should succeed before currency validation.");

        // Step 2 (Assignment): Click "Desktops" -> "Show all Desktops".
        SearchPage searchPage = new SearchPage(driver).goToDesktops();

        // Step 3 (Assignment): By default the prices are shown in dollars.
        List<String> usdPrices = searchPage.getAllProductPrices();
        Assert.assertFalse(usdPrices.isEmpty(), "Desktop prices should be visible before the currency change.");

        // Step 4 (Assignment): Change the currency to Euro.
        home.header().changeCurrencyToEuro();

        // Step 5 (Assignment): Verify the prices change accordingly.
        List<String> euroPrices = searchPage.getAllProductPrices();
        Assert.assertNotEquals(usdPrices, euroPrices, "Prices should change after the currency update.");

        // Step 6 (Assignment): Logout.
        home.header().logout();
    }
}
