package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class ResetPasswordPage {
    WebDriver driver;

    public ResetPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    public void submitEmail(String email) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // 1. Wait until the URL is definitely correct
        wait.until(ExpectedConditions.urlContains("forgot-password"));

        // 2. Use a generic locator to find ANY input that looks like an email field
        // This bypasses issues where the 'name' attribute might be loading slowly
        By locator = By.xpath("//input[@type='email' or @name='email']");

        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        // 3. Force focus and input using JavaScript (highest reliability)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", input);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", input); // Clear
        input.sendKeys(email);

        // 4. Find and click the button specifically by its "Send" text
        WebElement sendBtn = driver.findElement(By.xpath("//button[contains(text(),'Send')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sendBtn);
    }
}