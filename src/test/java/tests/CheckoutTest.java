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
        ExcelReader reader = new ExcelReader("testdata/testdata.xlsx", "Login");
        Object[][] data = reader.getData();

        for (Object[] row : data) {
            if (row[2].toString().equalsIgnoreCase("valid")) {
                validEmail = row[0].toString();
                validPassword = row[1].toString();
                break;
            }
        }
    }

    @Test
    public void testFullCheckoutFlow() {

        HomePage home = new HomePage(driver);

        // 1- Login
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(validEmail, validPassword);
        Assert.assertTrue(account.isAccountPageDisplayed());

        // 2- Go to MP3 Players
        SearchPage search = new SearchPage(driver);
        search.goToMP3Players();

        // 3- Add ipod shuffle
        ProductPage product = search.clickProduct("iPod Shuffle");
        product.addToCart();

        // 4- Verify success message
        String successMsg = search.getAddToCartSuccessMessage();
        Assert.assertTrue(successMsg.toLowerCase().contains("success"),
                "Success message not displayed");

        // 5 & 6- Open cart and verify item
        CartPage cart = home.header().goToShoppingCart();

        Assert.assertTrue(cart.isProductInCart("iPod Shuffle"),
                "Product not found in cart");

        String productPrice = cart.getProductPrice("iPod Shuffle");
        Assert.assertNotNull(productPrice);

        // Save total for later validation
        String totalBeforeCheckout = cart.getTotalPrice();

        // 7- Checkout
        CheckoutPage checkout = cart.proceedToCheckout();

        // 8- Fill billing (NEW ADDRESS)
        checkout.fillBillingDetails(
                "Test",
                "User",
                "Street 123",
                "Cairo",
                "12345",
                "Egypt",
                "Cairo"
        );

        // 9 & 10- Continue
        checkout.useSameBillingAddress();

        // 11- Shipping details continue
        checkout.selectFlatShippingRate();

        // 12 & 13- Delivery method + comment
        checkout.addComments("Test order");

        // 14- Payment method
        checkout.agreeToTerms()
                .continueToConfirm();

        // 15 & 16- Confirm page validation
        // (Basic check - you should improve this later)
        String totalAfterCheckout = cart.getTotalPrice();
        Assert.assertNotNull(totalAfterCheckout);

        // 17- Confirm order
        checkout.confirmOrder();

        // 18- Verify success
        Assert.assertTrue(checkout.isOrderPlaced(),
                "Order was not placed successfully");

        // 19- Logout
        home.header().logout();
    }
}