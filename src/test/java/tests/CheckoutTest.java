package tests;

import base.BaseTest;
import com.framework.pages.*;
import com.framework.utils.ExcelReader;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.Duration;

public class CheckoutTest extends BaseTest {

    private String validEmail;
    private String validPassword;
    private WebDriverWait wait;

    @BeforeMethod
    public void getValidCredentials() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        ExcelReader reader = new ExcelReader("testdata/testdata.xlsx", "Login");
        Object[][] data = reader.getData();
        for (Object[] row : data) {
            if (row[2].toString().equalsIgnoreCase("valid")) {
                validEmail = row[0].toString();
                validPassword = row[1].toString();
                break;
            }
        }
        Allure.addAttachment("Credentials", "Email: " + validEmail);
    }

    @DataProvider(name = "checkoutData")
    public Object[][] getCheckoutData() {
        ExcelReader reader = new ExcelReader("testdata/testdata.xlsx", "Checkout");
        Object[][] data = reader.getData();

        Object[][] cleanData = new Object[data.length][10];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 10; j++) {
                cleanData[i][j] = (data[i][j] == null || data[i][j].toString().equals("null"))
                        ? "" : data[i][j].toString();
            }
        }
        return cleanData;
    }

    @Test(dataProvider = "checkoutData")
    public void testNormalCheckoutProcess(String firstName, String lastName, String address,
                                          String city, String postcode, String country,
                                          String zone, String productName, String comment,
                                          String expectedSuccessMessage) {

        HomePage home = new HomePage(driver);

        // Step 1: Login
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(validEmail, validPassword);
        Assert.assertTrue(account.isAccountPageDisplayed());

        // Step 2: Go to MP3 Players
        SearchPage searchPage = new SearchPage(driver);
        searchPage.goToMP3Players();

        // Step 3: Add product to cart
        By productLink = By.cssSelector("a[href*='product_id=43']"); // iPod Shuffle product link
        driver.findElement(productLink).click();
        driver.findElement(By.cssSelector("#button-cart")).click();

        // Step 4: Verify success message
        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        Assert.assertTrue(successMsg.getText().contains(productName));

        // Step 5-6: Go to cart and verify
        home.header().goToShoppingCart();
        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isProductInCart(productName));
        Assert.assertNotNull(cartPage.getProductPriceInCart(productName));

        // Step 7: Checkout
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        // Step 8-10: Fill billing details
        checkoutPage.fillBillingDetails(firstName, lastName, address, city, postcode, country, zone);
        Assert.assertTrue(checkoutPage.isAddressDropdownPopulated());

        // Step 11: Fill shipping details
        checkoutPage.fillShippingDetails(firstName, lastName, address, city, postcode, country, zone);

        // Step 12-13: Delivery method and comment
        Assert.assertTrue(checkoutPage.isDeliveryMethodSectionDisplayed());
        checkoutPage.selectFlatShippingRate();
        checkoutPage.addComments(comment);

        // Step 14: Terms and continue
        Assert.assertTrue(checkoutPage.isPaymentMethodSectionDisplayed());
        checkoutPage.agreeToTerms();
        checkoutPage.continueToConfirm();

        // Step 15-16: Verify confirm order
        Assert.assertTrue(checkoutPage.isConfirmOrderSectionDisplayed());
        Assert.assertNotNull(checkoutPage.getFlatShippingRateInConfirmOrder());

        // Step 17: Confirm order
        checkoutPage.confirmOrder();

        // Step 18: Verify success
        Assert.assertTrue(checkoutPage.isOrderPlaced());
        Assert.assertTrue(checkoutPage.isCartEmptyWidget());

        // Step 19: Logout
        home.header().logout();
    }
}
