package tests;

import base.BaseTest;
import com.framework.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BreadcrumbTest extends BaseTest {

    @Test
    public void testBreadcrumbAndLeftMenu() {
        HomePage home = new HomePage(driver);

        // Login first
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid("test@email.com", "Password123");

        // Go to Tablets
        SearchPage searchPage = new SearchPage(driver);
        searchPage.goToTablets();

        // Check breadcrumb
        Assert.assertTrue(searchPage.isBreadcrumbCorrect("Tablets"));

        // Check left menu highlight
        Assert.assertTrue(searchPage.isLeftMenuItemHighlighted("Tablets"));

        // Logout
        home.header().logout();
    }
}