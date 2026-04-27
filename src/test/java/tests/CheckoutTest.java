package tests;

import base.BaseTest;
import com.framework.pages.AccountPage;
import com.framework.pages.CartPage;
import com.framework.pages.CheckoutPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.ProductPage;
import com.framework.pages.SearchPage;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {
    private static final String CHECKOUT_PRODUCT = "HP LP3065";
    private static final String DELIVERY_DATE = "2026-05-10";

    private String validEmail;
    private String validPassword;

    @BeforeMethod
    public void loadValidCredentials() {
        String[] credentials = testDataSupport.getValidLoginCredentials();
        validEmail = credentials[0];
        validPassword = credentials[1];
        Allure.addAttachment("Credentials", "Using: " + validEmail);
    }

    @Test
    public void testLoggedInCheckoutForHpLaptop() {
        HomePage home = new HomePage(driver);

        // Step 1 (Assignment): Go to "My Account" -> Login.
        LoginPage loginPage = home.header().goToLogin();

        // Step 2 (Assignment): Enter valid Email/password.
        AccountPage accountPage = loginPage.loginValid(validEmail, validPassword);

        // Step 3 (Assignment): The user can login to the system.
        Assert.assertTrue(accountPage.isAccountPageDisplayed(), "Login failed for checkout flow.");

        // Step 4 (Assignment adaptation): Open shopping cart and clear any earlier items.
        CartPage cartPage = home.header().goToShoppingCart().clearCart();
        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should be empty before adding the assignment product.");

        // Step 5 (Assignment): Go to Laptops & Notebooks and focus on HP LP3065 only.
        driver.get(configReader.getRequiredProperty("base.url"));
        SearchPage searchPage = new SearchPage(driver).goToLaptops();

        // Step 6 (Assignment): Open HP LP3065 and add the required delivery date before adding to cart.
        ProductPage productPage = searchPage.clickProduct(CHECKOUT_PRODUCT);
        productPage.addToCart(DELIVERY_DATE);
        Assert.assertTrue(productPage.getSuccessMessage().contains(CHECKOUT_PRODUCT),
                "Expected the success message to mention HP LP3065.");

        // Step 7 (Assignment): Open shopping cart and verify the selected laptop is present.
        cartPage = home.header().goToShoppingCart();
        Assert.assertTrue(cartPage.isProductInCart(CHECKOUT_PRODUCT),
                "HP LP3065 should be present in the shopping cart.");

        // Step 8: Proceed to checkout as a logged-in user.
        CheckoutPage checkoutPage = cartPage.proceedToCheckout().continueCheckoutAsLoggedInUser();

        // Step 9: Verify that the signed-in billing details section is available instead of failing at checkout options.
        Assert.assertTrue(checkoutPage.isBillingDetailsDisplayed(),
                "The logged-in checkout page should display billing details for HP LP3065.");

        // Step 10: Log out of the system.
        home.header().logout();
    }
}
