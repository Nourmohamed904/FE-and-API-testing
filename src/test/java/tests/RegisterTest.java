package tests;

import base.BaseTest;
import com.framework.pages.*;
import com.framework.utils.ExcelReader;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RegisterTest extends BaseTest {

    @DataProvider(name = "registerData")
    public Object[][] getRegisterData() {
        ExcelReader reader = new ExcelReader("testdata/testdata.xlsx", "Register");
        Object[][] originalData = reader.getData();

        // Create a new array for modified data
        Object[][] modifiedData = new Object[originalData.length][7];

        // Generate unique data for THIS DataProvider call
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        int randomNum = new Random().nextInt(99999);
        String uniqueEmail = "autotest." + timestamp + "." + randomNum + "@example.com";
        String uniqueFirstName = "Auto" + timestamp;
        String uniqueLastName = "User" + randomNum;

        Allure.addAttachment("Generated Data for this run",
                "Email: " + uniqueEmail + "\n" +
                        "Name: " + uniqueFirstName + " " + uniqueLastName);

        for (int i = 0; i < originalData.length; i++) {
            // Copy all values from Excel
            for (int j = 0; j < 7; j++) {
                Object value = originalData[i][j];
                if (value == null || value.toString().equals("null")) {
                    modifiedData[i][j] = "";
                } else {
                    modifiedData[i][j] = value.toString();
                }
            }

            // Check if this is a success row (column index 5 = expected_result)
            String expectedResult = modifiedData[i][5].toString();
            if (expectedResult.equalsIgnoreCase("success")) {
                // Replace with unique generated data
                modifiedData[i][0] = uniqueFirstName;  // first_name
                modifiedData[i][1] = uniqueLastName;   // last_name
                modifiedData[i][2] = uniqueEmail;      // email
                Allure.addAttachment("Row " + (i+1) + " Modified",
                        "Using email: " + uniqueEmail);
            }
        }

        return modifiedData;
    }

    @Test(dataProvider = "registerData")
    public void testRegister(String firstName, String lastName, String email,
                             String telephone, String password,
                             String expectedResult, String expectedErrorField) {

        Allure.addAttachment("Test Input",
                "First Name: '" + firstName + "'\n" +
                        "Last Name: '" + lastName + "'\n" +
                        "Email: '" + email + "'\n" +
                        "Telephone: '" + telephone + "'\n" +
                        "Expected: " + expectedResult + "\n" +
                        "Error Field: " + expectedErrorField);

        HomePage home = new HomePage(driver);
        RegisterPage register = home.header().goToRegister();

        if (expectedResult.equalsIgnoreCase("success")) {
            // Verify we have valid data
            if (email == null || email.isEmpty()) {
                Assert.fail("Email is null or empty! Generated data not applied properly.");
            }

            Allure.addAttachment("Action", "Attempting registration with: " + email);

            AccountPage account = register.registerSuccess(firstName, lastName, email, telephone, password);

            // Wait for page to load
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Check success by URL or page content
            String currentUrl = driver.getCurrentUrl();
            String pageSource = driver.getPageSource();

            boolean success = currentUrl.contains("account/success") ||
                    currentUrl.contains("account/account") ||
                    pageSource.contains("Your Account Has Been Created") ||
                    pageSource.contains("My Account");

            if (!success) {
                Allure.addAttachment("Current URL", currentUrl);
                Allure.addAttachment("Page Title", driver.getTitle());
                Allure.addAttachment("Page Source Snippet",
                        pageSource.length() > 500 ? pageSource.substring(0, 500) : pageSource);
            }

            Assert.assertTrue(success, "Registration failed for email: " + email);
            Allure.addAttachment("Result", "✅ Registration successful");

        } else {
            // Error test cases
            Allure.addAttachment("Action", "Testing error case for: " + expectedErrorField);
            register.registerWithErrors(firstName, lastName, email, telephone, password);

            boolean errorFound = false;
            String actualErrorField = "";

            if (register.isFirstNameErrorDisplayed()) {
                errorFound = true;
                actualErrorField = "firstname";
                Allure.addAttachment("Error Detected", "First name error");
            } else if (register.isLastNameErrorDisplayed()) {
                errorFound = true;
                actualErrorField = "lastname";
                Allure.addAttachment("Error Detected", "Last name error");
            } else if (register.isEmailErrorDisplayed()) {
                errorFound = true;
                actualErrorField = "email";
                Allure.addAttachment("Error Detected", "Email error");
            } else if (register.isTelephoneErrorDisplayed()) {
                errorFound = true;
                actualErrorField = "telephone";
                Allure.addAttachment("Error Detected", "Telephone error");
            } else if (register.isPasswordErrorDisplayed()) {
                errorFound = true;
                actualErrorField = "password";
                Allure.addAttachment("Error Detected", "Password error");
            }

            if (expectedErrorField == null || expectedErrorField.isEmpty()) {
                Assert.assertTrue(errorFound, "Expected some validation error but none found");
            } else {
                Assert.assertEquals(actualErrorField, expectedErrorField.toLowerCase(),
                        "Expected error for '" + expectedErrorField + "' but got error for '" + actualErrorField + "'");
            }
            Allure.addAttachment("Result", "✅ Error displayed for: " + actualErrorField);
        }
    }

    @AfterMethod
    public void cleanup() {
        // Logout if needed
        try {
            if (driver.getCurrentUrl().contains("account")) {
                HomePage home = new HomePage(driver);
                home.header().logout();
            }
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }
}