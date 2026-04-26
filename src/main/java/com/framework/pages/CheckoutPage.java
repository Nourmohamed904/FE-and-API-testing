package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Billing Details Section
    private final By billingFirstName = By.id("input-payment-firstname");
    private final By billingLastName = By.id("input-payment-lastname");
    private final By billingAddress1 = By.id("input-payment-address-1");
    private final By billingCity = By.id("input-payment-city");
    private final By billingPostcode = By.id("input-payment-postcode");
    private final By billingCountry = By.id("input-payment-country");
    private final By billingZone = By.id("input-payment-zone");
    private final By billingContinueBtn = By.id("button-payment-address");

    // Shipping Details Section
    private final By shippingFirstName = By.id("input-shipping-firstname");
    private final By shippingLastName = By.id("input-shipping-lastname");
    private final By shippingAddress1 = By.id("input-shipping-address-1");
    private final By shippingCity = By.id("input-shipping-city");
    private final By shippingPostcode = By.id("input-shipping-postcode");
    private final By shippingCountry = By.id("input-shipping-country");
    private final By shippingZone = By.id("input-shipping-zone");
    private final By shippingContinueBtn = By.id("button-shipping-address");

    // Delivery Method Section
    private final By flatShippingRate = By.cssSelector("input[name='shipping_method'][value='flat.flat']");
    private final By shippingMethodContinue = By.id("button-shipping-method");
    private final By commentsTextarea = By.tagName("textarea");

    // Payment Method Section
    private final By termsAgreeCheckbox = By.name("agree");
    private final By paymentMethodContinue = By.id("button-payment-method");

    // Confirm Order Section
    private final By confirmOrderBtn = By.id("button-confirm");
    private final By orderSuccessMessage = By.cssSelector("#content h1");
    private final By flatRatePrice = By.xpath("//table[@class='table table-bordered']//tfoot//td[contains(text(),'Flat Shipping Rate')]/following-sibling::td");

    // Existing Address Dropdown
    private final By existingAddressDropdown = By.name("address_id");
    private final By newAddressRadio = By.cssSelector("input[value='new']");

    // Billing Details
    public CheckoutPage fillBillingDetails(String firstName, String lastName,
                                           String address, String city,
                                           String postcode, String country, String zone) {
        click(newAddressRadio);
        waitForElement(billingFirstName);
        type(billingFirstName, firstName);
        type(billingLastName, lastName);
        type(billingAddress1, address);
        type(billingCity, city);
        type(billingPostcode, postcode);

        Select countrySelect = new Select(driver.findElement(billingCountry));
        countrySelect.selectByVisibleText(country);

        wait.until(d -> d.findElement(billingZone).isEnabled());
        Select zoneSelect = new Select(driver.findElement(billingZone));
        zoneSelect.selectByVisibleText(zone);

        click(billingContinueBtn);
        return this;
    }

    public CheckoutPage useExistingAddress(String addressValue) {
        Select addressSelect = new Select(driver.findElement(existingAddressDropdown));
        addressSelect.selectByVisibleText(addressValue);
        click(billingContinueBtn);
        return this;
    }

    // Shipping Details
    public CheckoutPage fillShippingDetails(String firstName, String lastName,
                                            String address, String city,
                                            String postcode, String country, String zone) {
        type(shippingFirstName, firstName);
        type(shippingLastName, lastName);
        type(shippingAddress1, address);
        type(shippingCity, city);
        type(shippingPostcode, postcode);

        Select countrySelect = new Select(driver.findElement(shippingCountry));
        countrySelect.selectByVisibleText(country);

        wait.until(d -> d.findElement(shippingZone).isEnabled());
        Select zoneSelect = new Select(driver.findElement(shippingZone));
        zoneSelect.selectByVisibleText(zone);

        click(shippingContinueBtn);
        return this;
    }

    public CheckoutPage useSameBillingAddress() {
        // Click on "Use same address" radio button if exists
        By sameAddressRadio = By.cssSelector("input[value='same']");
        if (driver.findElements(sameAddressRadio).size() > 0) {
            click(sameAddressRadio);
        }
        click(shippingContinueBtn);
        return this;
    }

    // Delivery Method
    public CheckoutPage selectFlatShippingRate() {
        click(flatShippingRate);
        return this;
    }

    public CheckoutPage addComments(String comment) {
        type(commentsTextarea, comment);
        click(shippingMethodContinue);
        return this;
    }

    public CheckoutPage proceedToPayment() {
        click(shippingMethodContinue);
        return this;
    }

    // Payment Method
    public CheckoutPage agreeToTerms() {
        click(termsAgreeCheckbox);
        return this;
    }

    public CheckoutPage continueToConfirm() {
        click(paymentMethodContinue);
        return this;
    }

    // Confirm Order
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

    public String getFlatShippingRate() {
        return getText(flatRatePrice);
    }

    // Combined checkout flow for normal checkout process
    public String completeNormalCheckout(String billingFirstName, String billingLastName,
                                         String billingAddress, String billingCity,
                                         String billingPostcode, String billingCountry,
                                         String billingZone, String comment) {

        // Step 1: Fill billing details
        fillBillingDetails(billingFirstName, billingLastName, billingAddress,
                billingCity, billingPostcode, billingCountry, billingZone);

        // Step 2: Shipping details (use same as billing or fill new)
        useSameBillingAddress();

        // Step 3: Delivery method
        selectFlatShippingRate();
        addComments(comment);

        // Step 4: Payment method
        agreeToTerms();
        continueToConfirm();

        // Step 5: Confirm order
        confirmOrder();

        return getOrderSuccessMessage();
    }
}