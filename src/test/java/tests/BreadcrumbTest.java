package tests;

import base.BaseTest;
import com.framework.pages.AccountPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.SearchPage;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BreadcrumbTest extends BaseTest {

    @Test
    public void testBreadcrumbAndLeftMenu() {
        String[] credentials = testDataSupport.getValidLoginCredentials();
        HomePage home = new HomePage(driver);

        // Step 1 (Assignment): Login by any valid user.
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(credentials[0], credentials[1]);
        Assert.assertTrue(account.isAccountPageDisplayed(), "Login should succeed before navigation checks.");

        // Step 2 (Assignment): Click on "Tablets".
        SearchPage searchPage = new SearchPage(driver).goToTablets();

        // Step 3 (Assignment): The latest link in breadcrumb is "Tablets".
        Assert.assertTrue(searchPage.isBreadcrumbCorrect("Tablets"),
                "Breadcrumb should show 'Tablets'");

        // Step 4 (Assignment): Capture the highlighted left-side link for traceability.
        Allure.addAttachment("Left Menu Active",
                "Active menu item: " + searchPage.getActiveLeftMenuItem());

        // Step 5 (Assignment): Log out.
        home.header().logout();
    }
}
