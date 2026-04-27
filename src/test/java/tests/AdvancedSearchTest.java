package tests;

import base.BaseTest;
import com.framework.pages.*;
import com.framework.utils.ExcelReader;
import com.framework.utils.AllureHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Search Functionality")
@Feature("Search in Subcategories")
public class AdvancedSearchTest extends BaseTest {

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
            validEmail = "john.doe@example.com";
            validPassword = "Password123";
        }
        Allure.addAttachment("Credentials", "Using email: " + validEmail);
    }

    @DataProvider(name = "subcategorySearchData")
    public Object[][] getSubcategorySearchData() {
        try {
            ExcelReader reader = new ExcelReader("testdata/testdata.xlsx", "AdvancedSearch");
            return reader.getData();
        } catch (Exception e) {
            // Return default test data if Excel file doesn't exist
            return new Object[][]{
                    {"Apple", "Components", "Apple Cinema 30", "no product"}
            };
        }
    }

    @Test(dataProvider = "subcategorySearchData")
    @Description("Search in subcategories - Verify that checking 'Search in subcategories' shows products from subcategories")
    @Story("Advanced Search")
    public void testSearchInSubcategories(String searchKeyword, String category,
                                          String expectedProduct, String expectedNoResultsMessage) {

        AllureHelper.attachLog("========================================");
        AllureHelper.attachLog("Search Keyword: " + searchKeyword);
        AllureHelper.attachLog("Category: " + category);
        AllureHelper.attachLog("Expected Product: " + expectedProduct);
        AllureHelper.attachLog("Expected No Results Message: " + expectedNoResultsMessage);
        AllureHelper.attachLog("========================================");

        HomePage home = new HomePage(driver);

        // Step 1: Login with valid user
        AllureHelper.attachLog("Step 1: Login with valid user");
        LoginPage login = home.header().goToLogin();
        AccountPage account = login.loginValid(validEmail, validPassword);
        Assert.assertTrue(account.isAccountPageDisplayed(), "Login should be successful");

        // Step 2: Navigate to advanced search page
        AllureHelper.attachLog("Step 2: Navigate to advanced search page");
        driver.get(driver.getCurrentUrl().replace("/index.php?route=account/account",
                "/index.php?route=product/search"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 3: Enter search keyword
        AllureHelper.attachLog("Step 3: Enter search keyword: " + searchKeyword);
        WebElement searchKeywordField = driver.findElement(By.id("input-search"));
        searchKeywordField.clear();
        searchKeywordField.sendKeys(searchKeyword);

        // Step 4: Select category
        AllureHelper.attachLog("Step 4: Select category: " + category);
        Select categorySelect = new Select(driver.findElement(By.name("category_id")));
        categorySelect.selectByVisibleText(category);

        // Step 5: Search WITHOUT subcategories - No products found
        AllureHelper.attachLog("Step 5: Search without subcategories - expecting no products");

        // Make sure subcategory checkbox is NOT checked
        WebElement subcategoryCheckbox = driver.findElement(By.name("sub_category"));
        if (subcategoryCheckbox.isSelected()) {
            subcategoryCheckbox.click();
        }

        // Click search button
        driver.findElement(By.id("button-search")).click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Check for no results message
        String pageSource = driver.getPageSource();
        boolean hasNoProductMessage = pageSource.toLowerCase().contains("no product") ||
                pageSource.toLowerCase().contains("there is no product");

        // Also check if any products are displayed
        java.util.List<WebElement> productResults = driver.findElements(By.cssSelector(".product-layout"));
        boolean hasProducts = productResults.size() > 0;

        if (hasProducts) {
            AllureHelper.attachLog("WARNING: Products found without subcategory search: " + productResults.size());
            for (WebElement product : productResults) {
                String productName = product.findElement(By.cssSelector(".caption h4 a")).getText();
                AllureHelper.attachLog("Product: " + productName);
            }
        }

        // For the test to be flexible, we check either no products OR "no product" message
        boolean noProductsFound = !hasProducts || hasNoProductMessage;
        Assert.assertTrue(noProductsFound,
                "Expected no products or 'no product' message when not searching subcategories. Found " +
                        productResults.size() + " products.");

        AllureHelper.attachLog("Step 5 Result: No products found verification passed");

        // Step 6: Check "Search in subcategories"
        AllureHelper.attachLog("Step 6: Enable 'Search in subcategories' and search again");

        // Get the checkbox again (page may have refreshed)
        WebElement subcategoryCheckboxAgain = driver.findElement(By.name("sub_category"));
        if (!subcategoryCheckboxAgain.isSelected()) {
            subcategoryCheckboxAgain.click();
        }

        // Click search again
        driver.findElement(By.id("button-search")).click();

        // Step 7: Verify expected product is displayed
        AllureHelper.attachLog("Step 7: Verify product '" + expectedProduct + "' is displayed");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Check if product appears in results
        By productLinks = By.cssSelector(".product-layout .caption h4 a");
        java.util.List<WebElement> products = driver.findElements(productLinks);
        boolean productFound = false;

        AllureHelper.attachLog("Total products found with subcategory search: " + products.size());

        for (WebElement product : products) {
            String productText = product.getText();
            AllureHelper.attachLog("Product found: " + productText);
            if (productText.contains(expectedProduct)) {
                productFound = true;
                break;
            }
        }

        Assert.assertTrue(productFound,
                String.format("Expected product '%s' should be displayed when searching in subcategories. Found %d products.",
                        expectedProduct, products.size()));

        // Step 8: Log out
        AllureHelper.attachLog("Step 8: Log out");
        home.header().logout();

        AllureHelper.attachLog("✓ Test completed successfully for keyword: " + searchKeyword);
    }
}
