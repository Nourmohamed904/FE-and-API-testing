package tests;

import base.BaseTest;
import com.framework.pages.*;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    private String validEmail;
    private String validPassword;

    @BeforeMethod
    public void getValidCredentials() {
        // Use demo credentials (they work on the site)
        validEmail = "demo@tutorialsninja.com";
        validPassword = "demo";
        Allure.addAttachment("Credentials", "Using: " + validEmail);
    }

    @Test
    public void testNormalCheckoutProcess() {
        Allure.addAttachment("Test Scenario", "Normal Checkout process and confirm order");

        HomePage home = new HomePage(driver);

        // Step 1: Login
        LoginPage login = home.header().goToLogin();
        login.loginValid(validEmail, validPassword);
        Allure.addAttachment("Step 1", "Login successful");

        // Step 2: Direct search for HP LP3065 (in-stock product)
        SearchPage searchPage = home.header().search("HP LP3065");
        Assert.assertTrue(searchPage.hasResults(), "HP LP3065 should be found in search");
        Allure.addAttachment("Step 2", "Search for HP LP3065 successful");

        // Step 3: Click on the product
        ProductPage productPage = searchPage.clickProduct("HP LP3065");
        Allure.addAttachment("Step 3", "Opened HP LP3065 product page");

        // Step 4: Add product to cart
        productPage.addToCart();
        Allure.addAttachment("Step 4", "Added HP LP3065 to cart");

        // Wait for cart to update
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 5: Get success message
        String successMsg = productPage.getSuccessMessage();
        Allure.addAttachment("Success Message", successMsg);
        Assert.assertTrue(successMsg.contains("Success"), "Product should be added to cart successfully");

        // Step 6: Go to cart and verify item is there
        CartPage cart = home.header().goToShoppingCart();
        int itemCount = cart.getCartItemCount();
        Assert.assertTrue(itemCount > 0, "Cart should have at least 1 item. Found: " + itemCount);
        Allure.addAttachment("Step 6", "Cart contains " + itemCount + " item(s)");

        // Step 7: Verify HP LP3065 is in cart
        boolean productInCart = cart.isProductInCart("HP LP3065");
        Assert.assertTrue(productInCart, "HP LP3065 should be in cart");
        Allure.addAttachment("Step 7", "Verified HP LP3065 is in cart");

        // Step 8: Proceed to checkout
        CheckoutPage checkout = cart.proceedToCheckout();
        Allure.addAttachment("Step 8", "Proceeded to checkout");

        // Wait for checkout page to load
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 9: Fill billing details
        String billingFirstName = "Test";
        String billingLastName = "User";
        String billingAddress = "123 Test Street";
        String billingCity = "New York";
        String billingPostcode = "10001";
        String billingCountry = "United States";
        String billingZone = "New York";
        checkout.fillBillingDetails(billingFirstName, billingLastName, billingAddress,
                billingCity, billingPostcode, billingCountry, billingZone);
        Allure.addAttachment("Step 9", "Billing details filled");

        // Step 10: Continue through checkout steps
        checkout.useSameBillingAddress();
        Allure.addAttachment("Step 10", "Used same billing address");

        checkout.selectFlatShippingRate();
        Allure.addAttachment("Step 11", "Selected flat shipping rate");

        checkout.addComments("Test order - please deliver during business hours");
        Allure.addAttachment("Step 12", "Added comments");

        checkout.agreeToTerms();
        Allure.addAttachment("Step 13", "Agreed to terms");

        checkout.continueToConfirm();
        Allure.addAttachment("Step 14", "Continued to confirm");

        // Step 15: Confirm order
        checkout.confirmOrder();
        Allure.addAttachment("Step 15", "Order confirmed");

        // Wait for order confirmation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 16: Verify order success
        String orderSuccessMessage = checkout.getOrderSuccessMessage();
        Allure.addAttachment("Order Success Message", orderSuccessMessage);
        Assert.assertTrue(checkout.isOrderPlaced(),
                "Order should be placed successfully. Message: " + orderSuccessMessage);

        // Step 17: Logout
        home.header().logout();
        Allure.addAttachment("Result", "✅ Checkout test passed!");
    }
}