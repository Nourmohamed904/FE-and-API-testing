package tests;

import base.BaseTest;
import com.framework.pages.*;
import com.framework.utils.ExcelReader;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BreadcrumbTest extends BaseTest {

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
            Allure.addAttachment("Credentials", "Using email: " + validEmail);
        } catch (Exception e) {
            Allure.addAttachment("Error", "Could not load credentials: " + e.getMessage());
            // Use default credentials as fallback
            validEmail = "john.doe@example.com";
            validPassword = "Password123";
        }
    }

    @Test
    public void testBreadcrumbAndLeftMenu() {
        HomePage home = new HomePage(driver);

        // Login with credentials from Excel
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(validEmail, validPassword);

        // Go to Tablets
        SearchPage searchPage = new SearchPage(driver);
        searchPage.goToTablets();

        // Check breadcrumb
        Assert.assertTrue(searchPage.isBreadcrumbCorrect("Tablets"),
                "Breadcrumb should show 'Tablets'");

        // Check left menu highlight
        Assert.assertTrue(searchPage.isLeftMenuItemHighlighted("Tablets"),
                "Left menu should highlight 'Tablets'");

        // Logout
        home.header().logout();
    }
}