package tests;

import base.BaseTest;
import com.framework.pages.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelReader;

public class RegisterTest extends BaseTest {

    @DataProvider(name = "registerData")
    public Object[][] getRegisterData() {
        ExcelReader reader = new ExcelReader("testdata.xlsx", "Register");
        return reader.getData();
    }

    @Test(dataProvider = "registerData")
    public void testRegister(String fName, String lName, String email,
                             String phone, String password) {

        HomePage home = new HomePage(driver);
        RegisterPage register = home.header().goToRegister();

        AccountPage account = register.register(fName, lName, email, phone, password);

        Assert.assertTrue(account.isAccountPageDisplayed());
    }
}