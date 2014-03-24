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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class TestIt {
    private WebDriver driver = null;

    @Before
    public void setup() throws Exception {
        driver = new SelendroidDriver(new SelendroidCapabilities("com.droid4you.application.swypenews:1.1"));
    }

    @Test
    public void assertUserAccountCanRegistered() throws Exception {
        // Initialize test data
        driver.switchTo().window("NATIVE_APP");
        int i;
        for (i = 1; i <10; i++) {


            new Actions(driver).sendKeys(SelendroidKeys.MENU).perform();
            driver.findElement(By.linkText("Nastavení")).click();
            driver.findElement(By.linkText("Ok"));
        }
    }


    @After
    public void teardown() {
        driver.quit();
    }
}
