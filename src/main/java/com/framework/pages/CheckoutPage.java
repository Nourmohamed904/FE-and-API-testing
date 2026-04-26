package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    private final By billingFirstName = By.id("input-payment-firstname");
    private final By billingLastName = By.id("input-payment-lastname");
    private final By billingAddress1 = By.id("input-payment-address-1");
    private final By billingCity = By.id("input-payment-city");
    private final By billingPostcode = By.id("input-payment-postcode");
    private final By billingCountry = By.id("input-payment-country");
    private final By billingZone = By.id("input-payment-zone");
    private final By billingContinueBtn = By.id("button-payment-address");
    private final By shippingContinueBtn = By.id("button-shipping-address");
    private final By flatShippingRate = By.cssSelector("input[name='shipping_method'][value='flat.flat']");
    private final By shippingMethodContinue = By.id("button-shipping-method");
    private final By commentsTextarea = By.name("comment");
    private final By termsAgreeCheckbox = By.name("agree");
    private final By paymentMethodContinue = By.id("button-payment-method");
    private final By confirmOrderBtn = By.id("button-confirm");
    private final By orderSuccessMessage = By.cssSelector("#content h1");
    private final By newAddressRadio = By.cssSelector("input[value='new']");
    private final By sameAddressRadio = By.cssSelector("input[value='same']");

    public CheckoutPage fillBillingDetails(String firstName, String lastName,
                                           String address, String city,
                                           String postcode, String country, String zone) {
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
        return this;
    }

    public CheckoutPage useSameBillingAddress() {
        if (areElementsPresent(sameAddressRadio)) {
            click(sameAddressRadio);
        }
        click(shippingContinueBtn);
        return this;
    }

    public CheckoutPage selectFlatShippingRate() {
        click(flatShippingRate);
        return this;
    }

    public CheckoutPage addComments(String comment) {
        if (comment != null && !comment.isEmpty()) {
            type(commentsTextarea, comment);
        }
        click(shippingMethodContinue);
        return this;
    }

    public CheckoutPage agreeToTerms() {
        click(termsAgreeCheckbox);
        return this;
    }

    public CheckoutPage continueToConfirm() {
        click(paymentMethodContinue);
        return this;
    }

    public CheckoutPage confirmOrder() {
        click(confirmOrderBtn);
        return this;
    }

    public String getOrderSuccessMessage() {
        return getText(orderSuccessMessage);
    }

    public boolean isOrderPlaced() {
        return getOrderSuccessMessage().contains("Your order has been placed");
    }
}