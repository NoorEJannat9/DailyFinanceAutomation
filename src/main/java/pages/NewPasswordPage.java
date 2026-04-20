package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class NewPasswordPage {
    WebDriver driver;

    By newPasswordField = By.xpath("//label[contains(text(),'New Password')]/following-sibling::div/input");
    By confirmPasswordField = By.xpath("//label[contains(text(),'Confirm Password')]/following-sibling::div/input");
    By resetBtn = By.xpath("//button[contains(.,'RESET')]");

    public NewPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setNewPassword(String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement newPassInput = wait.until(ExpectedConditions.elementToBeClickable(newPasswordField));
        newPassInput.sendKeys(password);
        driver.findElement(confirmPasswordField).sendKeys(password);

        try {
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(resetBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            WebElement fallback = driver.findElement(By.cssSelector("button[type='submit']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", fallback);
        }
    }
}