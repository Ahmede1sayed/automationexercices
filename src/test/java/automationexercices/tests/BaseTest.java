package automationexercices.tests;
import automationexercices.drivers.GUIDriver;
import automationexercices.drivers.WebDriverProvider;
import automationexercices.utils.DataReader.JsonReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;

public class BaseTest implements WebDriverProvider {
    protected GUIDriver driver;
    protected JsonReader testData;


    @Override
    public WebDriver getWebDriver() {
        return driver.get();
    }
}
