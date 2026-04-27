package tests;

import base.BaseTest;
import com.framework.pages.AccountPage;
import com.framework.pages.HomePage;
import com.framework.pages.RegisterPage;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RegisterTest extends BaseTest {

    @DataProvider(name = "registerData")
    public Object[][] getRegisterData() {
        Object[][] originalData = getTestDataSupport().getSheetData("Register");
        Object[][] modifiedData = new Object[originalData.length][7];

        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        int randomNum = new Random().nextInt(99999);
        String uniqueEmail = "autotest." + timestamp + "." + randomNum + "@example.com";
        String uniqueFirstName = "Auto" + timestamp;
        String uniqueLastName = "User" + randomNum;

        for (int i = 0; i < originalData.length; i++) {
            for (int j = 0; j < 7; j++) {
                Object value = originalData[i][j];
                modifiedData[i][j] = value == null ? "" : value.toString();
            }

            if ("success".equalsIgnoreCase(modifiedData[i][5].toString())) {
                modifiedData[i][0] = uniqueFirstName;
                modifiedData[i][1] = uniqueLastName;
                modifiedData[i][2] = uniqueEmail;
            }
        }

        Allure.addAttachment("Generated registration user", uniqueEmail);
        return modifiedData;
    }

    @Test(dataProvider = "registerData")
    public void testRegister(String firstName, String lastName, String email,
                             String telephone, String password,
                             String expectedResult, String expectedErrorField) {
        HomePage home = new HomePage(driver);
        RegisterPage register = home.header().goToRegister();

        if (expectedResult.equalsIgnoreCase("success")) {
            // Step 1-7 : Open Register, fill all required fields, accept agreement, continue, and verify success.
            AccountPage account = register.registerSuccess(firstName, lastName, email, telephone, password);
            Assert.assertTrue(account.isRegistrationSuccessDisplayed() || account.isAccountPageDisplayed(),
                    "Registration should succeed for generated email: " + email);
            Assert.assertTrue(account.isLogoutDisplayed(), "Logout should be available after successful registration.");
        } else {
            // Step 1-6 : Leave required fields blank or use a short password and verify the matching validation message.
            register.registerWithErrors(firstName, lastName, email, telephone, password);

            boolean errorFound = false;
            String actualErrorField = "";

            if (register.isFirstNameErrorDisplayed()) {
                errorFound = true;
                actualErrorField = "firstname";
            } else if (register.isLastNameErrorDisplayed()) {
                errorFound = true;
                actualErrorField = "lastname";
            } else if (register.isEmailErrorDisplayed()) {
                errorFound = true;
                actualErrorField = "email";
            } else if (register.isTelephoneErrorDisplayed()) {
                errorFound = true;
                actualErrorField = "telephone";
            } else if (register.isPasswordErrorDisplayed()) {
                errorFound = true;
                actualErrorField = "password";
            }

            if (expectedErrorField == null || expectedErrorField.isBlank()) {
                Assert.assertTrue(errorFound, "Expected a validation error but none was displayed.");
            } else {
                Assert.assertEquals(actualErrorField, expectedErrorField.toLowerCase(),
                        "Expected error for '" + expectedErrorField + "' but got '" + actualErrorField + "'.");
            }
        }
    }

}
