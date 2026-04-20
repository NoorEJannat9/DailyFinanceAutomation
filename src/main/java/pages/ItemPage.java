package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class ItemPage {
    WebDriver driver;

    By addCostBtn = By.xpath("//button[contains(.,'Add Cost')]");
    By itemNameField = By.id("itemName");
    By amountField = By.id("amount");
    By submitBtn = By.xpath("//button[@type='submit']");
    By searchBox = By.xpath("//input[@placeholder='Search items...']");

    public ItemPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addItem(String name, String amount, String remarks, boolean fillAll) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(addCostBtn)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(itemNameField)).sendKeys(name);
        driver.findElement(amountField).sendKeys(amount);
        driver.findElement(submitBtn).click();

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No alert present.");
        }
    }

    public boolean isItemVisible(String itemName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        search.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        search.sendKeys(itemName);

        try {
            By tableRow = By.xpath("//table//td[contains(text(),'" + itemName + "')]");
            return driver.findElements(tableRow).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}