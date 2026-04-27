package tests;

import base.BaseTest;
import com.framework.pages.AccountPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.SearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SortTest extends BaseTest {

    @Test
    public void testSortByName() {
        String[] credentials = testDataSupport.getValidLoginCredentials();
        HomePage home = new HomePage(driver);

        // Step 1 (Assignment): Login by any valid user.
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(credentials[0], credentials[1]);
        Assert.assertTrue(account.isAccountPageDisplayed(), "Login should succeed before sorting.");

        // Step 2 (Assignment): Click on "Phones & PDAs".
        SearchPage searchPage = new SearchPage(driver).goToPhonesAndPDAs();

        // Step 3 and 4 (Assignment): Sort by name A-Z and verify ascending order.
        searchPage.sortByNameAZ();
        Assert.assertTrue(searchPage.isSortedAscending(searchPage.getAllProductNames()),
                "Products should be sorted A to Z");

        // Step 5 and 6 (Assignment): Sort by name Z-A and verify descending order.
        searchPage.sortByNameZA();
        Assert.assertTrue(searchPage.isSortedDescending(searchPage.getAllProductNames()),
                "Products should be sorted Z to A");

        // Step 7 (Assignment): Logout.
        home.header().logout();
    }
}
