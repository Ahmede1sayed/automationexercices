package automationexercices.drivers;

import automationexercices.utils.DataReader.PropertyReader;
import org.openqa.selenium.WebDriver;

import java.io.File;

public abstract class AbstractDriver {
    protected final String remoteHost = PropertyReader.getProperty("remoteHost");
    protected final String remotePort = PropertyReader.getProperty("remotePort");
    protected File haramBlurExtension = new File("src/main/resources/extensions/HaramBlur.crx");
    protected String downloadsPath = System.getProperty("user.dir") + "\\src\\test\\resources";
    public abstract WebDriver createDriver();
}
