package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class RegistrationPage {
    WebDriver driver;

    By emailField = By.xpath("//label[contains(text(),'Email')]/following-sibling::div/input");

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
    }

    public void register(String fName, String lName, String email,
                         String password, String phone, String address) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(By.xpath("//label[contains(text(),'First Name')]/following-sibling::div/input")).sendKeys(fName);
        driver.findElement(By.xpath("//label[contains(text(),'Last Name')]/following-sibling::div/input")).sendKeys(lName);

        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(emailField));
        emailInput.click();
        emailInput.sendKeys(Keys.CONTROL + "a");
        emailInput.sendKeys(Keys.BACK_SPACE);
        emailInput.sendKeys(email);

        driver.findElement(By.xpath("//label[contains(text(),'Password')]/following-sibling::div/input")).sendKeys(password);
        driver.findElement(By.xpath("//label[contains(text(),'Phone Number')]/following-sibling::div/input")).sendKeys(phone);
        driver.findElement(By.xpath("//label[contains(text(),'Address')]/following-sibling::div/input")).sendKeys(address);
        driver.findElement(By.xpath("//input[@value='Male']")).click();
        driver.findElement(By.xpath("//input[@type='checkbox']")).click();
        driver.findElement(By.id("register")).click();
    }

    public boolean isCongratsMessageDisplayed() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(text(),'successfully') or contains(text(),'Congratulations') or contains(text(),'registered')]")
                    ))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}