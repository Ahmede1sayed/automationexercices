# TAF — Test Automation Framework

A Java/Selenium/TestNG UI and API automation framework for [automationexercise.com](https://automationexercise.com), built with the Page Object Model, JSON-driven test data, Allure reporting, Log4j2 logging, and REST-assured API calls for fast test setup/teardown.

## Overview

The framework covers the full customer journey on automationexercise.com across several independent and chained test suites:

| Test class | What it covers | Structure |
|---|---|---|
| `RegisterTest` | Sign-up flow, duplicate-account error handling | Independent `@Test`s |
| `LoginTest` | Valid login, invalid email, invalid password | Independent `@Test`s |
| `ProductsTest` | Product search + detail validation, add-to-cart without login | Independent `@Test`s |
| `ProductDetailsTest` | Product detail page validation, review submission | Independent `@Test`s |
| `CartTest` | Cart contents validation (name, price, quantity, total) | Independent `@Test`s |
| `CheckoutTest` | Register → login → add to cart → checkout, address verification | Chained via `dependsOnMethods` |
| `PaymentTest` | Register → login → add to cart → checkout → payment | Chained via `dependsOnMethods` |
| `InvoiceTest` | Full flow above + invoice download and file verification | Chained via `dependsOnMethods` |
| `RegisterTestAPI` | Pure API-level user registration (no browser) | Independent `@Test` |

User accounts used in UI flows are created and deleted via direct **API calls** (`UserManagementAPI`) rather than through the UI, keeping test setup/teardown fast and independent of UI flakiness.

## Tech Stack

| Tool | Purpose |
|---|---|
| Java 21 | Language |
| Maven | Build & dependency management |
| Selenium 4.46.0 | Browser automation |
| TestNG 7.12.0 | Test runner & assertions |
| REST-assured 6.0.1 | API calls (user create/delete) |
| Allure 2.35.4 | Test reporting (`@Epic`/`@Feature`/`@Story`/`@Severity`/`@Owner` tagging) |
| AspectJ | Enables Allure step annotations |
| Log4j2 | Logging (console + file) |
| Apache POI | Excel/data handling |
| JAVE + video-recorder-testng | Optional test run video recording |
| JsonPath | Reading structured test data from JSON |
| JSoup | HTML parsing |

## Project Structure

```
src/
├── main/java/automationexercices/
│   ├── apis/                # UserManagementAPI, request Builder (REST-assured)
│   ├── drivers/              # AbstractDriver, GUIDriver (ThreadLocal<WebDriver>), Browser factory,
│   │                         # chromeFactory / edgeFactory, WebDriverProvider, @UITest marker annotation
│   ├── listeners/            # TestNGListeners — screenshots, logs, video, Allure hooks
│   ├── pages/                # Page Objects: SignupLoginPage, SignupPage, ProductsPage, ProductDetailsPage,
│   │   └── components/       # CartPage, CheckoutPage, PaymentPage, DeleteAccountPage, LogoutPage,
│   │                         # ContactUsPage, TestCasesPage, NavigationBarComponent
│   ├── utils/
│   │   ├── Actions/           # BrowserActions, ElementActions, AlertActions
│   │   ├── DataReader/        # JsonReader, PropertyReader
│   │   ├── Logs/              # LogsManager
│   │   ├── Media/             # ScreenshotsManager, ScreenRecordManager
│   │   ├── report/            # AllureAttachmentManager, AllureBinaryManager, AllureConstants,
│   │   │                      # AllureEnvironmentManager, AllureReportGenerator
│   │   ├── validations/       # BaseAssertion, Validation (hard), Verification (soft)
│   │   ├── OSUtils, TerminalUtils, TimeManager, WaitManager
│   └── FileUtils.java
├── main/resources/
│   ├── webapp.properties       # Browser type & execution type
│   ├── environment.properties  # Base API/Web URLs
│   ├── waits.properties        # Default explicit wait timeout
│   ├── video.properties        # Recording toggle & output folder
│   ├── seleniumGrid.properties # Remote Grid host/port
│   ├── allure.properties       # Results directory + auto-open toggle
│   ├── log4j2.properties       # Logging config
│   └── META-INF/services/org.testng.ITestNGListener   # Registers TestNGListeners as a service
└── test/
    ├── java/automationexercices/tests/
    │   ├── BaseTest.java        # Shared driver + JsonReader fields
    │   ├── ui/                  # All UI test classes (see table above)
    │   └── api/RegisterTestAPI.java
    └── resources/test-data/     # register-data.json, login-data.json, products-data.json,
                                  # product-details-data.json, cart-data.json, checkout-data.json

test-output/
├── Logs/            # Log file(s) per run
├── screenshots/     # Pass/fail/skip screenshots per test step
├── recordings/      # Video recordings (if enabled)
├── allure-results/  # Raw Allure result files
├── full-report/     # Generated multi-run Allure report (with history/trends)
└── reports/         # Timestamped single-file Allure reports (AllureReport_<timestamp>.html)
```

## Prerequisites

- **JDK 21**
- **Maven** (or your IDE's bundled Maven)
- **Microsoft Edge** installed (default browser — see [Configuration](#configuration) to change it)
- **Allure command-line tool**, if you want to view/regenerate HTML reports manually:
  ```powershell
  scoop install allure
  # or
  npm install -g allure-commandline
  ```

## Configuration

**Browser and execution type** — `src/main/resources/webapp.properties`
```properties
browserType=Edge
executionType=Local
```
- `browserType`: `Chrome`, `Firefox`, `Edge`, or `Safari`
- `executionType`: `Local`, `LocalHeadless`, or `Remote` (uses `seleniumGrid.properties` for host/port)

**Environment URLs** — `src/main/resources/environment.properties`
```properties
baseUrlApi=https://automationexercise.com/api/
baseUrlWeb=https://automationexercise.com
```

**Explicit wait timeout** — `src/main/resources/waits.properties`
```properties
DEFAULT_WAIT=15
```

**Video recording** — `src/main/resources/video.properties`
```properties
recordTests=false
video.folder=test-output/recordings
```
Set `recordTests=true` to enable per-test video capture via `video-recorder-testng`.

**Allure behavior** — `src/main/resources/allure.properties`
```properties
allure.results.directory=test-output/allure-results
OpenAllureReportAfterExecution=true
```
When `OpenAllureReportAfterExecution=true` and `executionType` contains "local", the report opens automatically in the default browser at the end of the run.

**Test data** — `src/test/resources/test-data/*.json`
Each test class loads its own JSON file via `new JsonReader("<file-name>")` in `@BeforeClass`, keeping data scoped per feature (registration, login, products, cart, checkout). Dynamic values (emails, names) are suffixed with a per-class run timestamp (`TimeManager.getSimpleTimestamp()`) to avoid collisions between runs.

## Running the Tests

**From an IDE (IntelliJ):**
Right-click any test class under `src/test/java/automationexercices/tests/ui` or `api`, or run the whole `tests` package to execute all suites.

**From the command line:**
```powershell
mvn clean test
```

## Viewing the Allure Report

Results are written to `test-output/allure-results`. If `OpenAllureReportAfterExecution=true`, a single-file HTML report is generated and opened automatically after each run, saved under `test-output/reports/` with a timestamped filename.

To generate/view manually:
```powershell
allure serve test-output/allure-results
```
Or for the persistent multi-run report with history/trends (`test-output/full-report`):
```powershell
allure generate test-output/allure-results -o test-output/full-report --clean
allure open test-output/full-report
```

## Logging

Log4j2 writes to both console and file (`test-output/Logs/`), registered via `LogsManager`. Logs, screenshots, and old Allure results are cleaned up by `TestNGListeners` at the start of each run so `test-output/` reflects only the most recent execution alongside the accumulated `full-report` history.

## Design Notes

- **WebDriver lifecycle** — `GUIDriver` wraps a `ThreadLocal<WebDriver>` (protected via Selenium's `ThreadGuard`) so each thread gets its own isolated, thread-safe browser session. Independent test classes use `@BeforeMethod`/`@AfterMethod` for a fresh session per test; chained flows (`CheckoutTest`, `PaymentTest`, `InvoiceTest`) use `@BeforeClass`/`@AfterClass` to share one session across the dependent steps.
- **API-driven setup/teardown** — `UserManagementAPI` (REST-assured) creates and deletes test user accounts directly via the site's API rather than through the UI, making test setup faster and independent of UI issues.
- **Assertions** — `Validation` provides hard (fail-fast) assertions; `Verification` provides soft assertions that collect multiple failures before reporting, both built on a shared `BaseAssertion`.
- **Allure tagging** — every test class is annotated with `@Epic`/`@Feature`/`@Story`/`@Severity`/`@Owner`, giving the Allure report meaningful grouping and ownership attribution (`Ahmed`, `Ashraf`) out of the box.
- **Custom `@UITest` marker annotation** — used to tag UI-driving test classes, likely consumed by `TestNGListeners` to apply UI-specific behavior (screenshots, video) only where relevant.
- **Waits** — centralized in `WaitManager`/`ElementActions` using Selenium's `FluentWait`, with a configurable default timeout via `waits.properties`.

## Known Limitations / Next Steps

- Ad/third-party content on automationexercise.com can intermittently overlap product cards and interfere with clicks; no ad-blocking extension or dismissal step is currently wired in.
- `os.properties` exists but is currently empty — reserved for future OS-specific configuration.
- No CI/CD pipeline (e.g. GitHub Actions) yet to run the suite and publish the Allure report automatically on push.
- No cross-browser matrix run (Chrome/Firefox/Edge in parallel) configured yet, though the `ThreadLocal` driver design already supports it.
