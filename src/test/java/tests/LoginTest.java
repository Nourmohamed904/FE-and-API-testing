package tests;

import base.BaseTest;
import com.framework.pages.AccountPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.utils.AllureHelper;
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
        Object[][] data = getTestDataSupport().getSheetData("Login");
        Object[][] cleanData = new Object[data.length][3];

        for (int i = 0; i < data.length; i++) {
            cleanData[i][0] = data[i][0] == null ? "" : data[i][0].toString();
            cleanData[i][1] = data[i][1] == null ? "" : data[i][1].toString();
            cleanData[i][2] = data[i][2] == null ? "" : data[i][2].toString();
        }

        return cleanData;
    }

    @Test(dataProvider = "loginData")
    @Description("Test login with valid and invalid credentials")
    @Story("User Login")
    public void testLogin(String email, String password, String expectedResult) {
        AllureHelper.attachLog("Email: " + email);
        AllureHelper.attachLog("Expected Result: " + expectedResult);

        HomePage home = new HomePage(driver);
        LoginPage login = home.header().goToLogin();

        if (expectedResult.equalsIgnoreCase("valid")) {
            // Step 1-4 (Assignment): Go to Login, enter valid credentials, and verify My Account opens.
            AccountPage account = login.loginValid(email, password);
            Assert.assertTrue(account.isAccountPageDisplayed(),
                    "Account page should be displayed for valid login. Email: " + email);

            // Step 5 (Assignment continuation): Log out after the successful login scenario.
            home.header().logout();
        } else {
            // Step 1-4 (Assignment): Go to Login, enter wrong credentials, and verify the warning message.
            login.loginInvalid(email, password);

            String errorMessage = login.getErrorMessage();
            boolean hasError = errorMessage.contains("Warning")
                    || errorMessage.contains("No match")
                    || errorMessage.contains("E-Mail");

            Assert.assertTrue(hasError,
                    "Error message should appear for invalid login. Got: '" + errorMessage + "'");
        }
    }
}
