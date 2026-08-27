package automationexercices.utils.Actions;

import automationexercices.utils.Logs.LogsManager;
import automationexercices.utils.WaitManager;
import org.openqa.selenium.WebDriver;

public class AlertActions {
    private final WebDriver driver;
    private final WaitManager waitManager;

    public AlertActions(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManager(driver);
    }

    //accept alert
    public void acceptAlert(){
        waitManager.fluentWait().until(d -> {
            try {
                driver.switchTo().alert().accept();
                return true;
            } catch (Exception e) {
                LogsManager.error("Failed to accept alert:" , e.getMessage());
                return false;
            }
        });

    }
    //dismiss alert
    public void dismissAlert(){
        waitManager.fluentWait().until(d -> {
            try {
                driver.switchTo().alert().dismiss();
                return true;
            } catch (Exception e) {
                LogsManager.error("Failed to dismiss alert:" , e.getMessage());

                return false;
            }
        });
    }
    //get alert text
    public String getAlertText(){
        return waitManager.fluentWait().until(d -> {
            try {
                String text = d.switchTo().alert().getText();
                return !text.isEmpty() ? text : null;
            } catch (Exception e) {
                LogsManager.error("Failed to get alert text:", e.getMessage());

                return null;
            }
        });
    }
    //set alert text
    public void setAlertText(String text) {
        waitManager.fluentWait().until(d -> {
            try {
                driver.switchTo().alert().sendKeys(text);
                LogsManager.info("Set alert text: " + text);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

}
