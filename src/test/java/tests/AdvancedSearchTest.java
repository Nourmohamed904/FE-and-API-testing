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
        ExcelReader reader = new ExcelReader("testdata/testdata.xlsx", "AdvancedSearch");
        return reader.getData();
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

        // Step 2-3: Go to advanced search and enter search keyword
        AllureHelper.attachLog("Step 2-3: Navigate to advanced search and enter keyword: " + searchKeyword);
        SearchPage searchPage = new SearchPage(driver);
        searchPage.goToAdvancedSearch();
        searchPage.setSearchKeyword(searchKeyword);

        // Step 4: Select category
        AllureHelper.attachLog("Step 4: Select category: " + category);
        searchPage.selectCategory(category);

        // Step 5: Search WITHOUT subcategories - No products found
        AllureHelper.attachLog("Step 5: Search without subcategories - expecting no products");
        searchPage.setSearchInSubcategories(false);
        searchPage.performAdvancedSearch();

        String actualNoResultsMessage = searchPage.getNoResultsMessageText();
        Assert.assertTrue(actualNoResultsMessage.toLowerCase().contains(expectedNoResultsMessage.toLowerCase()),
                String.format("Expected message to contain '%s' but got '%s'",
                        expectedNoResultsMessage, actualNoResultsMessage));
        AllureHelper.attachLog("Step 5 Result: " + actualNoResultsMessage);

        // Step 6: Check "Search in subcategories"
        AllureHelper.attachLog("Step 6: Enable 'Search in subcategories' and search again");
        searchPage.setSearchInSubcategories(true);
        searchPage.performAdvancedSearch();

        // Step 7: Verify expected product is displayed
        AllureHelper.attachLog("Step 7: Verify product '" + expectedProduct + "' is displayed");
        try {
            Thread.sleep(2000); // Wait for results to load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean productFound = searchPage.isProductDisplayed(expectedProduct);
        Assert.assertTrue(productFound,
                String.format("Expected product '%s' should be displayed when searching in subcategories",
                        expectedProduct));

        // Log all products found for debugging
        AllureHelper.attachLog("All Products Found: " + searchPage.getSearchResultProductNames().toString());

        // Step 8: Log out
        AllureHelper.attachLog("Step 8: Log out");
        home.header().logout();

        AllureHelper.attachLog("✓ Test completed successfully for keyword: " + searchKeyword);
    }
}
