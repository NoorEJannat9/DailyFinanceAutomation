package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class ProfilePage {
    WebDriver driver;

    By profileIcon = By.xpath("//button[@aria-label='account of current user']");
    By profileOption = By.xpath("//li[contains(text(),'Profile')]");
    By logoutOption = By.xpath("//li[contains(text(),'Logout')]");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToProfile() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(profileIcon)).click();
        try { Thread.sleep(1000); } catch (Exception e) {}
        wait.until(ExpectedConditions.elementToBeClickable(profileOption)).click();
        try { Thread.sleep(3000); } catch (Exception e) {}

        // Wait until profile page loads (URL contains /user/)
        wait.until(ExpectedConditions.urlContains("/user/"));
        try { Thread.sleep(1000); } catch (Exception e) {}
    }

    public void updateEmail(String newEmail) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Click EDIT button — try multiple possible texts
        WebElement editBtn = null;
        String[] editLocators = {
                "//button[text()='EDIT']",
                "//button[text()='Edit']",
                "//button[contains(@class,'edit')]",
                "//button[contains(text(),'EDIT')]",
                "//button[contains(text(),'Edit')]"
        };

        for (String loc : editLocators) {
            try {
                editBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(loc)));
                System.out.println("EDIT button found: " + loc);
                break;
            } catch (Exception e) {
                System.out.println("Not found: " + loc);
            }
        }

        if (editBtn == null) {
            // Print all buttons on page for debugging
            java.util.List<WebElement> allBtns = driver.findElements(By.tagName("button"));
            System.out.println("All buttons on page:");
            for (WebElement b : allBtns) {
                System.out.println("  Button text: [" + b.getText() + "]");
            }
            throw new RuntimeException("EDIT button not found!");
        }

        js.executeScript("arguments[0].click();", editBtn);
        try { Thread.sleep(2000); } catch (Exception e) {}

        // Find email field and update
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.name("email")));

        js.executeScript("arguments[0].removeAttribute('readonly')", emailInput);
        js.executeScript("arguments[0].removeAttribute('disabled')", emailInput);
        js.executeScript("arguments[0].scrollIntoView(true);", emailInput);
        try { Thread.sleep(500); } catch (Exception e) {}

        js.executeScript(
                "var el = arguments[0];" +
                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(el, arguments[1]);" +
                        "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "el.dispatchEvent(new Event('change', { bubbles: true }));",
                emailInput, newEmail
        );
        try { Thread.sleep(500); } catch (Exception e) {}

        // Find and click UPDATE button
        WebElement updateBtn = null;
        String[] updateLocators = {
                "//button[text()='UPDATE']",
                "//button[text()='Update']",
                "//button[contains(text(),'UPDATE')]",
                "//button[contains(text(),'Update')]",
                "//button[@type='submit']"
        };

        for (String loc : updateLocators) {
            try {
                updateBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(loc)));
                System.out.println("UPDATE button found: " + loc);
                break;
            } catch (Exception e) {
                System.out.println("Update btn not found: " + loc);
            }
        }

        if (updateBtn == null) {
            java.util.List<WebElement> allBtns = driver.findElements(By.tagName("button"));
            System.out.println("All buttons after EDIT click:");
            for (WebElement b : allBtns) {
                System.out.println("  Button text: [" + b.getText() + "]");
            }
            throw new RuntimeException("UPDATE button not found!");
        }

        js.executeScript("arguments[0].click();", updateBtn);
        try { Thread.sleep(2000); } catch (Exception e) {}
    }

    public void logout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(profileIcon)).click();
            try { Thread.sleep(1000); } catch (Exception e) {}
            wait.until(ExpectedConditions.elementToBeClickable(logoutOption)).click();
            try { Thread.sleep(2000); } catch (Exception e) {}
        } catch (Exception e) {
            driver.get("https://dailyfinance.roadtocareer.net/login");
        }
    }
}