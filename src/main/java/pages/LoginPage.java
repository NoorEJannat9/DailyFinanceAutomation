package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    WebDriver driver;

    // Locators: Finding the email and password fields
    By emailField = By.name("email");
    By passwordField = By.name("password");
    By loginBtn = By.xpath("//button[text()='Login']");
    By registrationLink = By.partialLinkText("Register");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions: Methods to interact with the page
    public void clickRegister() {
        driver.findElement(registrationLink).click();
    }
}