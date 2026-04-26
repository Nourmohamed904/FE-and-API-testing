package tests;

import base.BaseTest;
import com.framework.pages.*;
import com.framework.utils.ExcelReader;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SortTest extends BaseTest {

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
    public void testSortByName() {
        HomePage home = new HomePage(driver);

        // Login with credentials from Excel
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(validEmail, validPassword);

        // Go to Phones & PDAs
        SearchPage searchPage = new SearchPage(driver);
        searchPage.goToPhonesAndPDAs();

        // Sort A to Z
        searchPage.sortByNameAZ();
        Assert.assertTrue(searchPage.isSortedAscending(searchPage.getAllProductNames()),
                "Products should be sorted A to Z");

        // Sort Z to A
        searchPage.sortByNameZA();
        Assert.assertTrue(searchPage.isSortedDescending(searchPage.getAllProductNames()),
                "Products should be sorted Z to A");

        // Logout
        home.header().logout();
    }
}