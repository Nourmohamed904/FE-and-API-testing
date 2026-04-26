package tests;

import base.BaseTest;
import com.framework.pages.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelReader;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        ExcelReader reader = new ExcelReader("testdata.xlsx", "Login");
        return reader.getData();
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String email, String password, String expected) {

        HomePage home = new HomePage(driver);
        LoginPage login = home.header().goToLogin();

        if (expected.equalsIgnoreCase("valid")) {
            AccountPage account = login.loginValid(email, password);
            Assert.assertTrue(account.isAccountPageDisplayed());
        } else {
            login.loginInvalid(email, password);
            Assert.assertTrue(login.getErrorMessage().contains("Warning"));
        }
    }
}