package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
        waitForPageLoad();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkout-checkout")));
    }

    private final By billingFirstName = By.id("input-payment-firstname");
    private final By billingLastName = By.id("input-payment-lastname");
    private final By billingAddress1 = By.id("input-payment-address-1");
    private final By billingCity = By.id("input-payment-city");
    private final By billingPostcode = By.id("input-payment-postcode");
    private final By billingCountry = By.id("input-payment-country");
    private final By billingZone = By.id("input-payment-zone");
    private final By billingContinueBtn = By.id("button-payment-address");

    private final By accountRegisterRadio = By.cssSelector("input[name='account'][value='register']");
    private final By accountGuestRadio = By.cssSelector("input[name='account'][value='guest']");
    private final By buttonAccount = By.id("button-account");
    private final By guestContinueButton = By.id("button-guest");

    private final By shippingContinueBtn = By.id("button-shipping-address");
    private final By sameAddressRadio = By.cssSelector("input[name='shipping_address'][value='1']");
    private final By newAddressRadio = By.cssSelector("input[name='shipping_address'][value='0']");

    private final By flatShippingRate = By.cssSelector("input[name='shipping_method'][value='flat.flat']");
    private final By shippingMethodContinue = By.id("button-shipping-method");

    private final By commentsTextarea = By.name("comment");
    private final By termsAgreeCheckbox = By.name("agree");
    private final By paymentMethodContinue = By.id("button-payment-method");
    private final By confirmOrderBtn = By.id("button-confirm");
    private final By orderSuccessMessage = By.cssSelector("#content h1");

    public CheckoutPage selectGuestCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(accountGuestRadio));
        click(accountGuestRadio);
        click(buttonAccount);
        wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));
        return this;
    }

    public CheckoutPage selectRegisterCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(accountRegisterRadio));
        click(accountRegisterRadio);
        click(buttonAccount);
        wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));
        return this;
    }

    public CheckoutPage continueCheckoutAsLoggedInUser() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));
        return this;
    }

    public boolean isBillingDetailsDisplayed() {
        return isDisplayed(billingFirstName);
    }

    public CheckoutPage fillBillingDetails(String firstName, String lastName,
                                           String address, String city,
                                           String postcode, String country, String zone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));
        type(billingFirstName, firstName);
        type(billingLastName, lastName);
        type(billingAddress1, address);
        type(billingCity, city);
        type(billingPostcode, postcode);

        Select countrySelect = new Select(waitForElement(billingCountry));
        countrySelect.selectByVisibleText(country);
        wait.until(d -> driver.findElement(billingZone).findElements(By.tagName("option")).size() > 1);
        selectZoneWithRetry(billingZone, zone);

        click(resolveBillingContinueButton());
        wait.until(driver -> areElementsPresent(shippingContinueBtn)
                || areElementsPresent(flatShippingRate)
                || areElementsPresent(termsAgreeCheckbox));
        return this;
    }

    public CheckoutPage useSameBillingAddress() {
        if (areElementsPresent(sameAddressRadio)) {
            click(sameAddressRadio);
        }
        if (areElementsPresent(shippingContinueBtn)) {
            click(shippingContinueBtn);
        }
        wait.until(driver -> areElementsPresent(flatShippingRate) || areElementsPresent(termsAgreeCheckbox));
        return this;
    }

    public CheckoutPage useNewShippingAddress(String firstName, String lastName,
                                              String address, String city,
                                              String postcode, String country, String zone) {
        if (areElementsPresent(newAddressRadio)) {
            click(newAddressRadio);
        }

        // Fill shipping address form
        type(By.id("input-shipping-firstname"), firstName);
        type(By.id("input-shipping-lastname"), lastName);
        type(By.id("input-shipping-address-1"), address);
        type(By.id("input-shipping-city"), city);
        type(By.id("input-shipping-postcode"), postcode);

        Select countrySelect = new Select(waitForElement(By.id("input-shipping-country")));
        countrySelect.selectByVisibleText(country);

        wait.until(d -> driver.findElement(By.id("input-shipping-zone")).findElements(By.tagName("option")).size() > 1);
        selectZoneWithRetry(By.id("input-shipping-zone"), zone);

        click(shippingContinueBtn);
        wait.until(driver -> areElementsPresent(flatShippingRate) || areElementsPresent(termsAgreeCheckbox));
        return this;
    }

    public CheckoutPage selectFlatShippingRate() {
        if (!areElementsPresent(flatShippingRate)) {
            return this;
        }
        wait.until(ExpectedConditions.elementToBeClickable(flatShippingRate));
        click(flatShippingRate);
        return this;
    }

    public CheckoutPage addCommentsAndContinue(String comment) {
        if (comment != null && !comment.isEmpty()) {
            type(commentsTextarea, comment);
        }
        click(shippingMethodContinue);
        wait.until(ExpectedConditions.presenceOfElementLocated(termsAgreeCheckbox));
        return this;
    }

    public CheckoutPage agreeToTerms() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(termsAgreeCheckbox));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }

    public CheckoutPage continueToConfirm() {
        click(paymentMethodContinue);
        wait.until(ExpectedConditions.presenceOfElementLocated(confirmOrderBtn));
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

    private void waitForPageLoad() {
        waitForDocumentReady();
    }

    private By resolveBillingContinueButton() {
        if (areElementsPresent(billingContinueBtn)) {
            return billingContinueBtn;
        }
        if (areElementsPresent(guestContinueButton)) {
            return guestContinueButton;
        }
        throw new IllegalStateException("No billing continue button is available on the checkout page.");
    }

    private void selectZoneWithRetry(By zoneLocator, String zone) {
        int attempts = 0;

        while (attempts < 3) {
            try {
                WebElement zoneElement = waitForElement(zoneLocator);
                if (selectZoneViaJavascript(zoneElement, zone)) {
                    return;
                }
            } catch (StaleElementReferenceException e) {
                wait.until(d -> driver.findElement(zoneLocator).findElements(By.tagName("option")).size() > 1);
            } catch (Exception e) {
                wait.until(d -> driver.findElement(zoneLocator).findElements(By.tagName("option")).size() > 1);
            }
            attempts++;
        }

        WebElement fallbackElement = waitForElement(zoneLocator);
        if (selectZoneViaJavascript(fallbackElement, zone)) {
            return;
        }

        throw new IllegalStateException("Unable to select a zone from " + zoneLocator);
    }

    private boolean selectZoneViaJavascript(WebElement selectElement, String zone) {
        Object selected = ((JavascriptExecutor) driver).executeScript(
                "const select = arguments[0];" +
                        "const target = arguments[1];" +
                        "const options = Array.from(select.options);" +
                        "let match = options.findIndex(option => option.text.trim() === target);" +
                        "if (match < 0) { match = options.findIndex(option => option.value && option.value !== ''); }" +
                        "if (match < 0) { return false; }" +
                        "select.selectedIndex = match;" +
                        "select.dispatchEvent(new Event('change', { bubbles: true }));" +
                        "return true;",
                selectElement,
                zone == null ? "" : zone.trim()
        );
        return Boolean.TRUE.equals(selected);
    }
}
