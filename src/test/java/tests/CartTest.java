package tests;

import base.BaseTest;
import com.framework.pages.AccountPage;
import com.framework.pages.CartPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.ProductPage;
import com.framework.pages.SearchPage;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test
    public void testAddItemsToCart() {
        String[] credentials = testDataSupport.getValidLoginCredentials();
        Allure.addAttachment("Credentials", "Using: " + credentials[0]);

        HomePage home = new HomePage(driver);

        // Step 1 : Login by any valid user.
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(credentials[0], credentials[1]);
        Assert.assertTrue(account.isAccountPageDisplayed(), "Login should succeed before cart validation.");

        // Step 2 : Clear the cart so only the in-stock laptop is validated.
        home.header().goToShoppingCart().clearCart();
        driver.get(configReader.getRequiredProperty("base.url"));

        // Step 3 : Go to Laptops & Notebooks and add HP LP3065 to the cart.
        SearchPage searchPage = new SearchPage(driver).goToLaptops();
        ProductPage productPage = searchPage.clickProduct("HP LP3065");
        productPage.addToCart("2026-05-10");
        Assert.assertTrue(productPage.getSuccessMessage().contains("HP LP3065"),
                "The add-to-cart success message should mention HP LP3065.");

        // Step 4 : Open shopping cart and check the item details.
        CartPage cart = home.header().goToShoppingCart();
        Assert.assertTrue(cart.isProductInCart("HP LP3065"), "HP LP3065 should appear in the cart.");

        // Step 5 : Check that the total price is displayed.
        String total = cart.getTotalPrice();
        Assert.assertNotNull(total, "Total price should be displayed");
        Allure.addAttachment("Total", total);

        // Step 6 : Log out.
        home.header().logout();
    }
}
