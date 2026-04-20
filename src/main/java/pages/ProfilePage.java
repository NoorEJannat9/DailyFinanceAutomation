package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class ProfilePage {
    WebDriver driver;

    By profileIcon = By.xpath("//button[@aria-label='account of current user']");
    By profileOption = By.xpath("//li[contains(text(),'Profile')]");
    By emailField = By.name("email");
    By updateBtn = By.xpath("//button[contains(text(),'Update')]");
    By logoutOption = By.xpath("//li[contains(text(),'Logout')]");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToProfile() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(profileIcon)).click();
        wait.until(ExpectedConditions.elementToBeClickable(profileOption)).click();
    }

    public void updateEmail(String newEmail) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailInput.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        emailInput.sendKeys(newEmail);
        driver.findElement(updateBtn).click();
    }

    public void logout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(profileIcon)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutOption)).click();
    }
}