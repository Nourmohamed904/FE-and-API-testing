package tests;

import base.BaseTest;
import com.framework.pages.*;
import com.framework.utils.ExcelReader;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

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
    public void testAddItemsToCart() {
        Allure.addAttachment("Test Scenario", "Add items to shopping cart and verify");

        HomePage home = new HomePage(driver);

        // Step 1: Login
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(validEmail, validPassword);
        Allure.addAttachment("Step 1", "Login successful");

        // Step 2: Search for a product
        SearchPage searchPage = home.header().search("iPhone");
        Allure.addAttachment("Step 2", "Searched for iPhone");

        // Step 3: Get product name from search results
        String productName = "iPhone";
        boolean hasResults = searchPage.hasResults();
        Assert.assertTrue(hasResults, "Search should return results for iPhone");

        // Step 4: Add product to cart using product page
        ProductPage productPage = searchPage.clickProduct(productName);
        productPage.addToCart();
        Allure.addAttachment("Step 4", "Added product to cart");

        // Step 5: Go to shopping cart
        CartPage cart = home.header().goToShoppingCart();
        Allure.addAttachment("Step 5", "Opened shopping cart");

        // Step 6: Verify cart is not empty
        int itemCount = cart.getCartItemCount();
        Assert.assertTrue(itemCount > 0, "Cart should have at least 1 item. Found: " + itemCount);
        Allure.addAttachment("Step 6", "Cart contains " + itemCount + " item(s)");

        // Step 7: Verify total price is displayed
        String total = cart.getTotalPrice();
        Assert.assertNotNull(total, "Total price should be displayed");
        Allure.addAttachment("Total", total);

        // Step 8: Logout
        home.header().logout();
        Allure.addAttachment("Result", "✅ Cart test passed!");
    }
}