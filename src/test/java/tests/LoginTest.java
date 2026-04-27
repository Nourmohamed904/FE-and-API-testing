package tests;

import base.BaseTest;
import com.framework.pages.*;
import com.framework.utils.AllureHelper;
import com.framework.utils.ExcelReader;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        ExcelReader reader = new ExcelReader("testdata/testdata.xlsx", "Login");
        Object[][] data = reader.getData();

        System.out.println("Total rows from Excel: " + data.length);

        // Print each row for debugging
        for (int i = 0; i < data.length; i++) {
            System.out.println("Row " + i + ": " +
                    "email=" + data[i][0] +
                    ", password=" + data[i][1] +
                    ", expected=" + data[i][2]);
        }

        // Clean up any null values - ONLY 3 columns
        Object[][] cleanData = new Object[data.length][3];
        for (int i = 0; i < data.length; i++) {
            cleanData[i][0] = (data[i][0] == null || data[i][0].toString().equals("null")) ? "" : data[i][0].toString();
            cleanData[i][1] = (data[i][1] == null || data[i][1].toString().equals("null")) ? "" : data[i][1].toString();
            cleanData[i][2] = (data[i][2] == null || data[i][2].toString().equals("null")) ? "" : data[i][2].toString();
        }

        return cleanData;
    }

    @Test(dataProvider = "loginData")
    @Description("Test login with valid and invalid credentials")
    @Story("User Login")
    public void testLogin(String email, String password, String expectedResult) {

        AllureHelper.attachLog("========================================");
        AllureHelper.attachLog("Email: " + email);
        AllureHelper.attachLog("Password: " + password);
        AllureHelper.attachLog("Expected Result: " + expectedResult);
        AllureHelper.attachLog("========================================");

        HomePage home = new HomePage(driver);
        LoginPage login = home.header().goToLogin();

        if (expectedResult.equalsIgnoreCase("valid")) {
            AllureHelper.attachLog("Attempting valid login");

            AccountPage account = login.loginValid(email, password);

            // Wait a moment for page to load
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            boolean isLoggedIn = account.isAccountPageDisplayed();
            Assert.assertTrue(isLoggedIn, "Account page should be displayed for valid login. Email: " + email);
            AllureHelper.attachLog("✅ Valid login successful for: " + email);

            // Logout
            home.header().logout();

        } else {
            AllureHelper.attachLog("Testing invalid login");
            login.loginInvalid(email, password);

            String errorMessage = login.getErrorMessage();
            boolean hasError = errorMessage.contains("Warning") ||
                    errorMessage.contains("No match") ||
                    errorMessage.contains("E-Mail");

            Assert.assertTrue(hasError,
                    "Error message should appear for invalid login. Got: '" + errorMessage + "'");
            AllureHelper.attachLog("✅ Invalid login correctly failed with message: " + errorMessage);
        }
    }
}