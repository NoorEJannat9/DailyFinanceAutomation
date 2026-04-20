package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginPage {
    WebDriver driver;

    // Locators
    By emailField = By.xpath("//input[@name='email' or @type='email']");
    By passwordField = By.xpath("//input[@name='password' or @type='password']");
    By loginBtn = By.xpath("//button[contains(.,'Login')]");
    // Added the missing locator for the register link
    By registerBtn = By.xpath("//a[contains(text(),'Register')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // This is the method that was missing!
    public void clickRegister() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(registerBtn)).click();
    }

    public void login(String email, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));

        // Clear and fill
        js.executeScript("arguments[0].value = '';", emailInput);
        emailInput.sendKeys(email);

        WebElement passInput = driver.findElement(passwordField);
        passInput.sendKeys(password);

        // Click login
        WebElement btn = driver.findElement(loginBtn);
        js.executeScript("arguments[0].click();", btn);
    }
}