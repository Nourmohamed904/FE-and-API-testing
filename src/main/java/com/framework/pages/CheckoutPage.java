package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends HomePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Billing details
    private final By billingFirstName = By.cssSelector("#input-payment-firstname");
    private final By billingLastName = By.cssSelector("#input-payment-lastname");
    private final By billingAddress1 = By.cssSelector("#input-payment-address-1");
    private final By billingCity = By.cssSelector("#input-payment-city");
    private final By billingPostcode = By.cssSelector("#input-payment-postcode");
    private final By billingCountry = By.cssSelector("#input-payment-country");
    private final By billingZone = By.cssSelector("#input-payment-zone");
    private final By billingContinueBtn = By.cssSelector("#button-payment-address");

    // Shipping method
    private final By flatShippingRate = By.cssSelector("input[value='flat.flat']");
    private final By shippingMethodContinue = By.cssSelector("#button-shipping-method");

    // Payment method
    private final By commentsTextarea = By.cssSelector("textarea[name='comment']");
    private final By termsAgreeCheckbox = By.cssSelector("input[name='agree']");
    private final By paymentMethodContinue = By.cssSelector("#button-payment-method");

    // Confirm order
    private final By confirmOrderBtn = By.cssSelector("#button-confirm");
    private final By orderSuccessMessage = By.cssSelector("#content h1");

    // Address options
    private final By newAddressRadio = By.cssSelector("input[value='new']");

    public void fillBillingDetails(String firstName, String lastName,
                                   String address, String city,
                                   String postcode, String country, String zone) {
        typeCheckoutInfo(firstName, lastName, address, city, postcode, country, zone, billingFirstName, billingLastName, billingAddress1, billingCity, billingPostcode, billingCountry, billingZone, billingContinueBtn);
    }

    private void typeCheckoutInfo(String firstName, String lastName, String address, String city, String postcode, String country, String zone, By billingFirstName, By billingLastName, By billingAddress1, By billingCity, By billingPostcode, By billingCountry, By billingZone, By billingContinueBtn) {
        if (areElementsPresent(newAddressRadio)) {
            click(newAddressRadio);
        }
        waitForElement(billingFirstName);
        type(billingFirstName, firstName);
        type(billingLastName, lastName);
        type(billingAddress1, address);
        type(billingCity, city);
        type(billingPostcode, postcode);

        Select countrySelect = new Select(waitForElement(billingCountry));
        countrySelect.selectByVisibleText(country);

        wait.until(d -> d.findElement(billingZone).isEnabled());
        Select zoneSelect = new Select(driver.findElement(billingZone));
        zoneSelect.selectByVisibleText(zone);

        click(billingContinueBtn);
    }

    public void selectFlatShippingRate() {
        click(flatShippingRate);
    }

    public void addComments(String comment) {
        if (comment != null && !comment.isEmpty()) {
            type(commentsTextarea, comment);
        }
        click(shippingMethodContinue);
    }

    public void agreeToTerms() {
        click(termsAgreeCheckbox);
    }

    public void continueToConfirm() {
        click(paymentMethodContinue);
    }

    public void confirmOrder() {
        click(confirmOrderBtn);
    }

    public String getOrderSuccessMessage() {
        return getText(orderSuccessMessage);
    }

    public boolean isOrderPlaced() {
        return getOrderSuccessMessage().contains("Your order has been placed");
    }

    public void useSameBillingAddress() {
        try {
            click(By.id("button-shipping-address"));
        } catch (Exception e) {
            // If not found, try alternative
            try {
                click(By.cssSelector("#button-shipping-address"));
            } catch (Exception ex) {
                // Step might not be needed
            }
        }
    }
}