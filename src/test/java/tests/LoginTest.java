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
        return reader.getData();
    }

    @Test(dataProvider = "loginData")
    @Description("Test login with valid and invalid credentials")
    @Story("User Login")
    public void testLogin(String email, String password, String expectedResult, String description) {
        AllureHelper.attachLog("Starting test: " + description);
        AllureHelper.attachLog("Email: " + email);
        AllureHelper.attachLog("Expected Result: " + expectedResult);

        HomePage home = new HomePage(driver);
        LoginPage login = home.header().goToLogin();

        if (expectedResult.equalsIgnoreCase("valid")) {
            AllureHelper.attachLog("Attempting valid login");
            AccountPage account = login.loginValid(email, password);
            Assert.assertTrue(account.isAccountPageDisplayed(),
                    "Account page should be displayed for valid login");
            AllureHelper.attachLog("Valid login successful - Account page displayed");
        } else {
            AllureHelper.attachLog("Attempting invalid login");
            login.loginInvalid(email, password);
            Assert.assertTrue(login.getErrorMessage().contains("Warning"),
                    "Error message should contain warning for invalid login");
            AllureHelper.attachLog("Invalid login failed correctly - Error message displayed");
        }
    }
}