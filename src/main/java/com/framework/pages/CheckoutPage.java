package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage {

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

    // Shipping details
    private final By shippingFirstName = By.cssSelector("#input-shipping-firstname");
    private final By shippingLastName = By.cssSelector("#input-shipping-lastname");
    private final By shippingAddress1 = By.cssSelector("#input-shipping-address-1");
    private final By shippingCity = By.cssSelector("#input-shipping-city");
    private final By shippingPostcode = By.cssSelector("#input-shipping-postcode");
    private final By shippingCountry = By.cssSelector("#input-shipping-country");
    private final By shippingZone = By.cssSelector("#input-shipping-zone");
    private final By shippingContinueBtn = By.cssSelector("#button-shipping-address");

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
    private final By addressDropdown = By.cssSelector("#input-payment-address");

    // Confirm order table
    private final By confirmOrderTotal = By.cssSelector(".table-bordered tfoot tr:last-child td:last-child");
    private final By flatShippingRateText = By.cssSelector(".table-bordered td:contains('Flat Shipping Rate') + td");

    // Cart widget
    private final By cartTotalWidget = By.cssSelector("#cart-total");

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

    public CheckoutPage fillShippingDetails(String firstName, String lastName,
                                            String address, String city,
                                            String postcode, String country, String zone) {
        if (areElementsPresent(newAddressRadio)) {
            click(newAddressRadio);
        }

        waitForElement(shippingFirstName);
        type(shippingFirstName, firstName);
        type(shippingLastName, lastName);
        type(shippingAddress1, address);
        type(shippingCity, city);
        type(shippingPostcode, postcode);

        Select countrySelect = new Select(waitForElement(shippingCountry));
        countrySelect.selectByVisibleText(country);

        wait.until(d -> d.findElement(shippingZone).isEnabled());
        Select zoneSelect = new Select(driver.findElement(shippingZone));
        zoneSelect.selectByVisibleText(zone);

        click(shippingContinueBtn);
        return this;
    }

    public boolean isAddressDropdownPopulated() {
        try {
            Select select = new Select(driver.findElement(addressDropdown));
            return select.getOptions().size() > 0;
        } catch (Exception e) {
            return false;
        }
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

    public boolean isDeliveryMethodSectionDisplayed() {
        return isDisplayed(flatShippingRate);
    }

    public boolean isPaymentMethodSectionDisplayed() {
        return isDisplayed(termsAgreeCheckbox);
    }

    public boolean isConfirmOrderSectionDisplayed() {
        return isDisplayed(confirmOrderBtn);
    }

    public String getConfirmOrderTotal() {
        return getText(confirmOrderTotal);
    }

    public String getFlatShippingRateInConfirmOrder() {
        return getText(flatShippingRateText);
    }

    public String getCartTotalFromWidget() {
        return getText(cartTotalWidget);
    }

    public boolean isCartEmptyWidget() {
        String cartText = getCartTotalFromWidget();
        return cartText.contains("0 item") || cartText.contains("0 items");
    }
}