package tests;

import base.BaseTest;
import com.framework.pages.*;
import com.framework.utils.ExcelReader;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

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
            validEmail = "demo@tutorialsninja.com";
            validPassword = "demo";
        }
        Allure.addAttachment("Credentials", "Using: " + validEmail);
    }

    @Test
    public void testFullCheckoutFlow() throws InterruptedException {
        Allure.addAttachment("Test", "Starting Full Checkout Flow");

        HomePage home = new HomePage(driver);

        // 1- Login
        Allure.addAttachment("Step 1", "Logging in");
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(validEmail, validPassword);
        Assert.assertTrue(account.isAccountPageDisplayed(), "Login failed");

        // 2- Clear cart first (important fix!)
        Allure.addAttachment("Step 2", "Clearing existing cart items");
        CartPage initialCart = home.header().goToShoppingCart();
        initialCart.clearCart();

        // Verify cart is empty
        Assert.assertTrue(initialCart.isCartEmpty(), "Cart should be empty after clearing");
        Allure.addAttachment("Cart Status", "Cart cleared successfully");

        // 3- Go to Laptops & Notebooks (has in-stock items)
        Allure.addAttachment("Step 3", "Navigating to Laptops & Notebooks");
        SearchPage search = new SearchPage(driver);
        search.goToLaptops();

        // Verify we have results
        Assert.assertTrue(search.hasResults(), "Laptops page should have products");
        Allure.addAttachment("Products Found", "Product count: " + search.getProductCount());

        // 4- Get first product name and add to cart
        String productName = search.getFirstProductName();
        Assert.assertNotNull(productName, "No products found on laptops page");
        Allure.addAttachment("Step 4", "Adding product to cart: " + productName);

        ProductPage product = search.clickProduct(productName);
        product.addToCart();

        // 5- Verify success message
        String successMsg = product.getSuccessMessage();
        Assert.assertTrue(successMsg.toLowerCase().contains("success"),
                "Success message not displayed. Got: " + successMsg);
        Allure.addAttachment("Cart Addition", "Success: " + successMsg);

        // 6- Open cart and verify item
        Allure.addAttachment("Step 5", "Verifying cart contains product");
        CartPage cart = home.header().goToShoppingCart();

        Assert.assertTrue(cart.isProductInCart(productName),
                "Product '" + productName + "' not found in cart");

        // 7- Checkout
        Allure.addAttachment("Step 6", "Proceeding to checkout");
        CheckoutPage checkout = cart.proceedToCheckout();

        // 8- Fill billing details
        Allure.addAttachment("Step 7", "Filling billing details");
        checkout.fillBillingDetails(
                "Test",
                "User",
                "123 Test Street",
                "Cairo",
                "12345",
                "Egypt",
                "Cairo"
        );

        // 9- Use same billing address
        Allure.addAttachment("Step 8", "Using same billing address");
        checkout.useSameBillingAddress();

        // 10- Select shipping rate
        Allure.addAttachment("Step 9", "Selecting flat shipping rate");
        checkout.selectFlatShippingRate();

        // 11- Add comments
        Allure.addAttachment("Step 10", "Adding comments and continuing");
        checkout.addComments("Automated test order - " + new java.util.Date());

        // 12- Agree to terms
        Allure.addAttachment("Step 11", "Agreeing to terms");
        checkout.agreeToTerms();

        // 13- Continue to confirm
        Allure.addAttachment("Step 12", "Continuing to payment method");
        checkout.continueToConfirm();

        // 14- Confirm order
        Allure.addAttachment("Step 13", "Confirming order");
        checkout.confirmOrder();

        // 15- Verify success
        Allure.addAttachment("Step 14", "Verifying order placement");
        Assert.assertTrue(checkout.isOrderPlaced(),
                "Order was not placed successfully");

        Allure.addAttachment("Result", "✅ Order placed successfully!");

        // 16- Logout
        Allure.addAttachment("Step 15", "Logging out");
        home.header().logout();
    }

    @Test
    public void testCheckoutWithHPProduct() throws InterruptedException {
        // Alternative test using HP LP3065 which is known to be in stock
        Allure.addAttachment("Test", "Checkout with HP LP3065");

        HomePage home = new HomePage(driver);

        // Login
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(validEmail, validPassword);
        Assert.assertTrue(account.isAccountPageDisplayed());

        // Clear cart first
        CartPage initialCart = home.header().goToShoppingCart();
        initialCart.clearCart();

        // Go to Laptops
        SearchPage search = new SearchPage(driver);
        search.goToLaptops();

        // Click HP LP3065
        ProductPage product = search.clickProduct("HP LP3065");
        product.addToCart();

        String successMsg = product.getSuccessMessage();
        Assert.assertTrue(successMsg.toLowerCase().contains("success"));

        // Proceed to checkout
        CartPage cart = home.header().goToShoppingCart();
        Assert.assertTrue(cart.isProductInCart("HP LP3065"));

        CheckoutPage checkout = cart.proceedToCheckout();

        checkout.fillBillingDetails(
                "Test", "User", "123 Street",
                "Cairo", "12345", "Egypt", "Cairo"
        );
        checkout.useSameBillingAddress();
        checkout.selectFlatShippingRate();
        checkout.addComments("Test order with HP LP3065");
        checkout.agreeToTerms();
        checkout.continueToConfirm();
        checkout.confirmOrder();

        Assert.assertTrue(checkout.isOrderPlaced(), "Order placement failed");

        home.header().logout();
    }
}