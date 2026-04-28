package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class AdminDashboardPage {
    WebDriver driver;

    By searchField = By.xpath("//input[@placeholder='Search...']");

    public AdminDashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean searchAndVerifyUser(String email) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement search = wait.until(ExpectedConditions.elementToBeClickable(searchField));
        search.clear();
        search.sendKeys(email);
        try {
            Thread.sleep(2000);
            return driver.findElements(
                    By.xpath("//td[contains(text(),'" + email + "')]")
            ).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}