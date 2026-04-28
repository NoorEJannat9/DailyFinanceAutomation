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
    By remarksField = By.id("remarks");
    By submitBtn = By.xpath("//button[@type='submit']");
    By searchBox = By.xpath("//input[@placeholder='Search items...']");

    public ItemPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addItem(String name, String amount, String remarks, boolean fillAllFields) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(addCostBtn)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(itemNameField)).sendKeys(name);
        driver.findElement(amountField).sendKeys(amount);

        // Fill remarks only if fillAllFields is true and remarks is provided
        if (fillAllFields && !remarks.isEmpty()) {
            try {
                driver.findElement(remarksField).sendKeys(remarks);
            } catch (Exception e) {
                System.out.println("Remarks field not found, skipping.");
            }
        }

        driver.findElement(submitBtn).click();

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("No alert present after adding item.");
        }
    }

    public boolean isItemVisible(String itemName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
            search.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
            search.sendKeys(itemName);
            By tableRow = By.xpath("//table//td[contains(text(),'" + itemName + "')]");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(tableRow)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}