package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
        // Wait for checkout page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("content")));
    }

    // Billing details - FIXED selectors based on actual HTML
    private final By billingFirstName = By.id("input-payment-firstname");
    private final By billingLastName = By.id("input-payment-lastname");
    private final By billingAddress1 = By.id("input-payment-address-1");
    private final By billingCity = By.id("input-payment-city");
    private final By billingPostcode = By.id("input-payment-postcode");
    private final By billingCountry = By.id("input-payment-country");
    private final By billingZone = By.id("input-payment-zone");
    private final By billingContinueBtn = By.id("button-payment-address");

    // Shipping address
    private final By shippingContinueBtn = By.id("button-shipping-address");

    // Shipping method - FIXED selector
    private final By flatShippingRate = By.cssSelector("input[name='shipping_method'][value='flat.flat']");
    private final By shippingMethodContinue = By.id("button-shipping-method");

    // Payment method
    private final By commentsTextarea = By.name("comment");
    private final By termsAgreeCheckbox = By.name("agree");
    private final By paymentMethodContinue = By.id("button-payment-method");
    private final By confirmOrderBtn = By.id("button-confirm");
    private final By orderSuccessMessage = By.cssSelector("#content h1");

    // Address options
    private final By newAddressRadio = By.cssSelector("input[value='new']");
    private final By sameAddressRadio = By.cssSelector("input[value='same']");

    // NEW: Wait for checkout steps
    private final By checkoutStep = By.cssSelector("#checkout-checkout");

    public CheckoutPage fillBillingDetails(String firstName, String lastName,
                                           String address, String city,
                                           String postcode, String country, String zone) throws InterruptedException {

        // Wait for billing section to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));

        // Select new address if option exists
        if (areElementsPresent(newAddressRadio)) {
            click(newAddressRadio);
            Thread.sleep(500);
        }

        // Fill form
        type(billingFirstName, firstName);
        type(billingLastName, lastName);
        type(billingAddress1, address);
        type(billingCity, city);
        type(billingPostcode, postcode);

        // Select country
        Select countrySelect = new Select(waitForElement(billingCountry));
        countrySelect.selectByVisibleText(country);

        // Wait for zone to be enabled and select
        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(billingZone));
        Select zoneSelect = new Select(driver.findElement(billingZone));
        zoneSelect.selectByVisibleText(zone);

        // Click continue
        click(billingContinueBtn);

        // Wait for next section
        Thread.sleep(1500);
        return this;
    }

    public CheckoutPage useSameBillingAddress() throws InterruptedException {
        if (areElementsPresent(sameAddressRadio)) {
            click(sameAddressRadio);
        }
        click(shippingContinueBtn);
        Thread.sleep(1000);
        return this;
    }

    public CheckoutPage selectFlatShippingRate() {
        wait.until(ExpectedConditions.elementToBeClickable(flatShippingRate));
        click(flatShippingRate);
        return this;
    }

    public CheckoutPage addComments(String comment) throws InterruptedException {
        if (comment != null && !comment.isEmpty()) {
            type(commentsTextarea, comment);
        }
        click(shippingMethodContinue);
        Thread.sleep(1000);
        return this;
    }

    public CheckoutPage agreeToTerms() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(termsAgreeCheckbox));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }

    public CheckoutPage continueToConfirm() throws InterruptedException {
        click(paymentMethodContinue);
        Thread.sleep(1000);
        return this;
    }

    public CheckoutPage confirmOrder() {
        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmOrderBtn));
        confirmBtn.click();
        return this;
    }

    public String getOrderSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderSuccessMessage));
        return getText(orderSuccessMessage);
    }

    public boolean isOrderPlaced() {
        String message = getOrderSuccessMessage();
        return message.contains("Your order has been placed") ||
                message.contains("Your order has been placed!");
    }

    // Helper method to add sleep (temporary - replace with proper waits)
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}