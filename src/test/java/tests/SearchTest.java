package tests;

import base.BaseTest;
import com.framework.pages.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.framework.utils.ExcelReader;

public class SearchTest extends BaseTest {

    @DataProvider(name = "searchData")
    public Object[][] getSearchData() {
        ExcelReader reader = new ExcelReader("testdata/testdata.xlsx", "Search");
        return reader.getData();
    }

    @Test(dataProvider = "searchData")
    public void testSearch(String product, String expected) {
        HomePage home = new HomePage(driver);
        SearchPage search = home.header().search(product);

        if (expected.equalsIgnoreCase("found")) {
            Assert.assertTrue(search.hasResults(), "Expected results to be found for: " + product);
        } else {
            String message = search.getNoResultsMessage();
            Assert.assertTrue(message.toLowerCase().contains("no product") || !search.hasResults(),
                    "Expected 'no product' message for: " + product + ". Got: " + message);
        }
    }
}