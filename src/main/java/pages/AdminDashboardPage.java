package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class AdminDashboardPage {
    WebDriver driver;

    public AdminDashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean searchAndVerifyUser(String email) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Print all inputs on page to find the right search field
        List<WebElement> allInputs = driver.findElements(By.tagName("input"));
        System.out.println("All inputs on admin page:");
        for (WebElement input : allInputs) {
            System.out.println("  Input placeholder: [" + input.getAttribute("placeholder") + "] type: [" + input.getAttribute("type") + "]");
        }

        // Try multiple search field locators
        WebElement searchField = null;
        String[] searchLocators = {
                "//input[@placeholder='Search...']",
                "//input[@placeholder='Search']",
                "//input[@placeholder='search']",
                "//input[@type='search']",
                "//input[contains(@placeholder,'Search') or contains(@placeholder,'search') or contains(@placeholder,'Email') or contains(@placeholder,'email')]",
                "//input[@type='text']"
        };

        for (String loc : searchLocators) {
            try {
                searchField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(loc)));
                System.out.println("Search field found: " + loc);
                break;
            } catch (Exception e) {
                System.out.println("Search field not found: " + loc);
            }
        }

        if (searchField == null) {
            System.out.println("No search field found! Checking if email is directly visible...");
            return driver.findElements(
                    By.xpath("//td[contains(text(),'" + email + "')]")
            ).size() > 0;
        }

        searchField.clear();
        searchField.sendKeys(email);

        try { Thread.sleep(2000); } catch (Exception e) {}

        List<WebElement> results = driver.findElements(
                By.xpath("//td[contains(text(),'" + email + "')]")
        );
        System.out.println("Search results found: " + results.size());
        return results.size() > 0;
    }
}