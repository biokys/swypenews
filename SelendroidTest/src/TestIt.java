/**
 * Created by erik on 12.3.14.
 */
import io.selendroid.SelendroidCapabilities;
import io.selendroid.SelendroidDriver;

import io.selendroid.SelendroidKeys;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import java.util.logging.Logger;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class TestIt {
    private WebDriver driver = null;
    WebElement settingsButton = null;
    WebElement clickMe = null;
    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
        catch (StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Čekání do načtení prvku
     *
     * @param element Prvek elementu By
     * @param timeout časový údaj (počet vteřin) po kterých má vypršet časový limit
     * @throws InterruptedException
     */
    private void waitForElementPresent(By element, int timeout, boolean isPresent) throws InterruptedException, TimeoutException {
        for (int second = 0; ; second++) {
            if (second >= timeout) {
                //ThreadLogger.log(this, "timeout: " + element.toString(), Level.ERROR);
                // fail("timeout: " + element.toString());
                throw new TimeoutException("Telefon se na necem zasekl");
            }
            try {
                if (isPresent ? isElementPresent(element) : !isElementPresent(element)) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }

    private void waitForElementPresent(By element, int timeout) throws InterruptedException, TimeoutException {
        waitForElementPresent(element, timeout, true);
    }

    private void waitForElementPresent(By element) throws InterruptedException, TimeoutException {
        waitForElementPresent(element, 30, true);
    }

    private void waitForElementNotPresent(By element, int timeout) throws InterruptedException, TimeoutException {
        waitForElementPresent(element, timeout, false);
    }

    private void waitForElementNotPresent(By element) throws InterruptedException, TimeoutException {
        waitForElementPresent(element, 30, false);
    }
    @Before
    public void setup() throws Exception {
        driver = new SelendroidDriver(new SelendroidCapabilities("com.droid4you.application.swypenews:1.1"));
    }

    @Test
    public void assertMenuClicked() throws Exception {
        // Initialize test data
        driver.switchTo().window("NATIVE_APP");
        Logger LOGGER = Logger.getLogger("InfoLogging");
        do {
            new Actions(driver).sendKeys(SelendroidKeys.MENU).perform();
            waitForElementPresent(By.partialLinkText("Nastavení"));
            settingsButton = driver.findElement(By.partialLinkText("Nastavení"));
            settingsButton.click();
            waitForElementPresent(By.partialLinkText("Ok"));
            clickMe = driver.findElement(By.partialLinkText("Ok"));
            LOGGER.info(clickMe.toString()+"\n");
            clickMe.click();
        } while (true);
        //Assert.assertTrue(clickMe.getText().contains("Ok"));
        //clickMe.click();
    }


    @After
    public void teardown() {
        driver.quit();
    }
}
