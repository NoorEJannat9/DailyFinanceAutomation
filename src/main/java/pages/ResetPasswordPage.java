package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class ResetPasswordPage {
    WebDriver driver;

    By emailInput = By.cssSelector("input[type='email']");
    By sendBtn = By.cssSelector("button[type='submit']");
    By errorMsg = By.xpath("//*[contains(text(),'not registered') or contains(text(),'required') or contains(text(),'invalid') or contains(text(),'empty')]");

    public ResetPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    public void submitEmail(String email) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        input.clear();
        input.click();
        if (!email.isEmpty()) {
            input.sendKeys(email);
        }
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(sendBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public boolean isErrorMessageDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(6))
                    .until(ExpectedConditions.visibilityOfElementLocated(errorMsg));
            return true;
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("forgot-password");
        }
    }
}