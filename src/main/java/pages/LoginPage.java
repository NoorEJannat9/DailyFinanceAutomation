package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginPage {
    WebDriver driver;

    By emailField = By.xpath("//input[@name='email' or @type='email']");
    By passwordField = By.xpath("//input[@name='password' or @type='password']");
    By loginBtn = By.xpath("//button[contains(.,'Login')]");
    By registerBtn = By.xpath("//a[contains(text(),'Register')]");
    By errorMessage = By.xpath("//*[contains(text(),'Invalid') or contains(text(),'incorrect') or contains(text(),'failed') or contains(text(),'wrong')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickRegister() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(registerBtn)).click();
    }

    public void login(String email, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        js.executeScript("arguments[0].value = '';", emailInput);
        emailInput.sendKeys(email);

        WebElement passInput = driver.findElement(passwordField);
        passInput.sendKeys(password);

        WebElement btn = driver.findElement(loginBtn);
        js.executeScript("arguments[0].click();", btn);
    }

    public boolean isLoginErrorDisplayed() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(errorMessage))
                    .isDisplayed();
        } catch (Exception e) {
            // Also check if URL still contains /login (i.e. login did not succeed)
            return driver.getCurrentUrl().contains("/login");
        }
    }
}