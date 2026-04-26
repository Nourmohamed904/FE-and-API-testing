package tests;

import base.BaseTest;
import com.framework.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SortTest extends BaseTest {

    @Test
    public void testSortByName() {
        HomePage home = new HomePage(driver);

        // Login first (as per Excel scenario)
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid("test@email.com", "Password123");

        // Go to Phones & PDAs
        SearchPage searchPage = new SearchPage(driver);
        searchPage.goToPhonesAndPDAs();

        // Sort A to Z
        searchPage.sortByNameAZ();
        Assert.assertTrue(searchPage.isSortedAscending(searchPage.getAllProductNames()));

        // Sort Z to A
        searchPage.sortByNameZA();
        Assert.assertTrue(searchPage.isSortedDescending(searchPage.getAllProductNames()));

        // Logout
        home.header().logout();
    }
}