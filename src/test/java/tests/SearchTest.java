package tests;

import base.BaseTest;
import com.framework.pages.AccountPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.SearchPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SearchTest extends BaseTest {

    @DataProvider(name = "searchData")
    public Object[][] getSearchData() {
        return getTestDataSupport().getSheetData("Search");
    }

    @Test(dataProvider = "searchData")
    public void testSearch(String product, String expected) {
        String[] credentials = getTestDataSupport().getValidLoginCredentials();
        HomePage home = new HomePage(driver);

        // Step 1 : Login by any valid user.
        LoginPage loginPage = home.header().goToLogin();
        AccountPage accountPage = loginPage.loginValid(credentials[0], credentials[1]);
        Assert.assertTrue(accountPage.isAccountPageDisplayed(), "Login should succeed before searching.");

        // Step 2 and 3 : Enter a product name in Search and submit.
        SearchPage search = home.header().search(product);

        // Step 4 : Verify the search results.
        if (expected.equalsIgnoreCase("found")) {
            Assert.assertTrue(search.hasResults(), "Expected results to be found for: " + product);
        } else {
            String message = search.getNoResultsMessage();
            Assert.assertTrue(message.toLowerCase().contains("no product") || !search.hasResults(),
                    "Expected 'no product' message for: " + product + ". Got: " + message);
        }

        // Step 5 : Log out.
        home.header().logout();
    }
}
