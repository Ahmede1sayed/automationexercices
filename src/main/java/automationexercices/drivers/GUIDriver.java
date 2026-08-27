package automationexercices.drivers;

import automationexercices.drivers.AbstractDriver;
import automationexercices.utils.Actions.AlertActions;
import automationexercices.utils.Actions.BrowserActions;
import automationexercices.utils.Actions.ElementActions;
import automationexercices.utils.Logs.LogsManager;
import automationexercices.utils.validations.Validation;
import automationexercices.utils.validations.Verification;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import automationexercices.utils.DataReader.PropertyReader;

public class GUIDriver {
    private final String browser = PropertyReader.getProperty("browserType");

    private ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    public GUIDriver(){
        LogsManager.info("Initializing GUIDriver with browser: " + browser);
        drivers.Browser browserType = drivers.Browser.valueOf(browser.toUpperCase());
        LogsManager.info("Starting driver for browser: " + browserType);
        AbstractDriver abstractDriver = browserType.getDriverFactory(); //local
        WebDriver driver = ThreadGuard.protect(abstractDriver.createDriver());
        driverThreadLocal.set(driver);
    }
    public ElementActions element() {
        return new ElementActions(get());
    }
    public BrowserActions browser() {
        return new BrowserActions(get());
    }

    public AlertActions alert() {
        return new AlertActions(get());
    }
    //soft assertions
    public Validation validation() {
        return new Validation(get());
    }
    // hard assertions
    public Verification verification() {
        return new Verification(get());
    }

    public WebDriver get() {
        return driverThreadLocal.get();
    }

    public void quitDriver() {
        driverThreadLocal.get().quit();
    }
}