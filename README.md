# Automation Testing Framework & API Testing

> **Part A:** Selenium WebDriver + Java automation framework for a live e-commerce demo site  
> **Part B:** Postman API test collection against the DummyJSON REST API


## Part A — UI Automation Framework

### Overview

A complete end-to-end automation testing framework built with Java and Selenium WebDriver, targeting the TutorialsNinja demo e-commerce website. The framework follows the **Page Object Model (POM)** design pattern and supports **Data-Driven Testing** by reading all test inputs from an external Excel sheet. Test results are visualised through an interactive **Allure Report** with logs and failure screenshots.

**Application Under Test:** [http://tutorialsninja.com/demo/index.php?route=common/home](http://tutorialsninja.com/demo/index.php?route=common/home)

---

### Tech Stack

| Tool | Purpose |
|---|---|
| Java | Primary programming language |
| Selenium WebDriver | Browser automation |
| TestNG | Test runner and assertions |
| Apache POI | Reading test data from Excel (.xlsx) |
| Allure Report | Interactive HTML test reporting |
| Maven | Build and dependency management |

---

### Project Structure

```
A2-PartA/
│
├── src/
│   ├── main/java/
│   │   ├── base/
│   │   │   ├── BaseTest.java            # Driver setup, teardown, TestNG hooks
│   │   │   └── TestDataSupport.java     # Bridge between Excel data and tests
│   │   │
│   │   ├── com/framework/
│   │   │   ├── pages/
│   │   │   │   ├── HomePage.java        # Base page — shared browser interactions
│   │   │   │   ├── AccountPage.java     # Post-login/registration page checks
│   │   │   │   ├── LoginPage.java       # Login form actions and validations
│   │   │   │   ├── RegisterPage.java    # Registration form actions and validations
│   │   │   │   ├── SearchPage.java      # Search results, breadcrumb, left menu
│   │   │   │   ├── CartPage.java        # Shopping cart operations
│   │   │   │   ├── CheckoutPage.java    # Full checkout flow
│   │   │   │   └── components/
│   │   │   │       └── HeaderComponent.java  # Navigation bar, logout, currency
│   │   │   │
│   │   │   └── utils/
│   │   │       ├── ConfigReader.java    # Reads config.properties
│   │   │       ├── ExcelReader.java     # Reads test data from Excel via Apache POI
│   │   │       └── AllureHelper.java    # Screenshots, logs, page source for Allure
│   │   │
│   │   └── resources/
│   │       └── config.properties        # Browser type, base URL, Excel file path
│   │
│   └── test/java/
│       └── tests/
│           ├── RegisterTest.java        # TC1, TC2 — Registration success and errors
│           ├── LoginTest.java           # TC3, TC4 — Valid and invalid login
│           ├── CurrencyTest.java        # TC5 — Currency switcher
│           ├── BreadcrumbTest.java      # TC6 — Breadcrumb and left menu highlight
│           ├── SortTest.java            # TC7 — Sort by name A-Z and Z-A
│           ├── SearchTest.java          # TC8 — Search by product name
│           ├── AdvancedSearchTest.java  # TC9 — Search in subcategories
│           ├── CartTest.java            # TC10 — Add items and verify totals
│           └── CheckoutTest.java        # TC11 — Full checkout and order confirmation
│
├── testdata/
│   └── testdata.xlsx                    # All test data (Login, Register, Search sheets)
│
├── testng.xml                           # TestNG suite configuration
└── pom.xml                              # Maven dependencies
```

---

### Test Scenarios

All 11 test cases from the assignment Excel sheet are fully automated:

| # | Test Case | Description |
|---|---|---|
| TC1 | Registration without errors | Fill all required fields, verify success message and Logout visibility |
| TC2 | Registration with errors | Submit incomplete form, verify field-level validation messages |
| TC3 | Valid login | Login with correct credentials, verify My Account page opens |
| TC4 | Invalid login | Login with wrong credentials, verify error message |
| TC5 | Change currency | Switch from USD to EUR, verify prices update accordingly |
| TC6 | Breadcrumb & left menu | Navigate to Tablets, verify breadcrumb and active sidebar link |
| TC7 | Sort by name | Sort Phones & PDAs A→Z and Z→A, verify order |
| TC8 | Search by name | Search "Mac", verify all results contain "Mac" |
| TC9 | Search in subcategories | Search "Apple" in Components without/with subcategory checkbox |
| TC10 | Add to cart & compare totals | Add two items, verify prices and cart total |
| TC11 | Checkout & confirm order | Full checkout flow, verify order confirmation message |

---

### Framework Architecture

#### Page Object Model (POM)

Each webpage is represented by its own Java class. Locators and actions are encapsulated inside the page class — tests never interact with the browser directly.

```
Test Class  →  Page Object  →  Browser (WebDriver)
```

If the website changes a button's location, only the relevant page class needs updating — no test code changes required.

#### Data-Driven Testing

Test data is read at runtime from `testdata.xlsx`. Each sheet corresponds to a test group (Login, Register, AdvancedSearch, etc.). Tests use TestNG `@DataProvider` to receive rows from Excel as parameters. **No test data is hardcoded in the source code.**

#### Configuration Externalisation

All environment-specific values live in `config.properties`:

```properties
browser=chrome
url=http://tutorialsninja.com/demo/index.php?route=common/home
testdata.filepath=testdata/testdata.xlsx
```

`ConfigReader.java` loads this file at startup and exposes values to the rest of the framework.

#### Locator Strategy

All element locators use strong, reliable strategies:

| Preferred | Acceptable | Forbidden |
|---|---|---|
| `By.id()` | `By.cssSelector()` | Absolute XPath (`/html/body/div[2]/...`) |
| `By.name()` | `By.linkText()` | Index-based XPath |
| Attribute-based CSS | Relative XPath with `contains()` | |

### Allure Report

After the test run completes, Allure results are generated in the `allure-results/` folder.

**Generate and open the report:**
```bash
allure serve allure-results
```

This opens an interactive HTML report in your browser showing:

- ✅ Passed / ❌ Failed / ⚠️ Skipped breakdown per test
- Step-by-step execution logs for every test
- Screenshots automatically captured on test failure
- Full page source attached on failure for debugging
- Epics, Features, and Stories hierarchy for organised navigation

> Screenshots are captured automatically by the TestNG Listener (`TestListener.java`) which hooks into `onTestFailure()` and calls `AllureHelper.takeScreenshot(driver)` without any manual intervention in test code.

---

## Part B — API Testing with Postman

### Overview

A Postman collection of 8 automated API tests targeting the DummyJSON REST API. The collection covers the full CRUD lifecycle, authentication token management, environment variable passing between requests, and negative testing.

**API Base URL:** [https://dummyjson.com](https://dummyjson.com)  

---

### Environment Variables

| Variable | Value | Purpose |
|---|---|---|
| `base_url` | `https://dummyjson.com` | Used in every request URL — never hardcoded |
| `auth_token` | *(auto-filled)* | Populated by the login test script, used by authenticated requests |

All request URLs are built using `{{base_url}}` to ensure the environment variable is always active.

---

### Test Cases

| # | Type | Endpoint | What Is Tested |
|---|---|---|---|
| 1 | GET | `/products/{id}` × 3 | Status 200, correct JSON body, response time under limit |
| 2 | POST | `/products/add` | Status 201, response contains sent title, response `id` is a number |
| 3 | POST | `/auth/login` (form-data) | Status 200, response body contains an `accessToken` string |
| 4 | ENV VAR | *(script only)* | Token from Test 3 saved to `auth_token` via `pm.environment.set()` |
| 5 | GET | `/auth/me` | Status 200, response contains correct user info and user ID |
| 6 | PUT | `/products/{id}` | Status 200, response reflects updated title and price values |
| 7 | DELETE | `/products/{id}` | Status 200, response `isDeleted` is `true` |
| 8 | Negative | `/products/{invalidId}` | Status 404 Not Found |

#### Example Test Script (Test 3 — Login)

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response contains accessToken", function () {
    const json = pm.response.json();
    pm.expect(json.accessToken).to.be.a("string");
});
```

#### Example Test Script (Test 4 — Save Token)

```javascript
const json = pm.response.json();
pm.environment.set("auth_token", json.accessToken);
```

#### Example Test Script (Test 7 — Delete)

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("isDeleted is true", function () {
    const json = pm.response.json();
    pm.expect(json.isDeleted).to.be.true;
});
```
