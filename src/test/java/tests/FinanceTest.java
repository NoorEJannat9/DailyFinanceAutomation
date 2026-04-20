package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import pages.utils.EmailUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class FinanceTest extends BaseTest {

    @Test
    public void testFullSystemFlow() throws Exception {
        // Page Object Initializations
        LoginPage loginPage = new LoginPage(driver);
        RegistrationPage regPage = new RegistrationPage(driver);
        ResetPasswordPage resetPage = new ResetPasswordPage(driver);
        NewPasswordPage newPassPage = new NewPasswordPage(driver);
        ItemPage itemPage = new ItemPage(driver);
        ProfilePage profilePage = new ProfilePage(driver);
        AdminDashboardPage adminPage = new AdminDashboardPage(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // --- STEP 1: REGISTRATION ---
        String uniqueUser = "noorejannat19+" + System.currentTimeMillis() + "@gmail.com";
        loginPage.clickRegister();
        regPage.register("Pinto", "Gomez", uniqueUser, "Password123!", "01712345678", "Dhaka");
        Assert.assertTrue(regPage.isCongratsMessageDisplayed(), "Registration failed!");

        // --- STEP 2 & 3: RESET PASSWORD REQUEST ---
        driver.get("https://dailyfinance.roadtocareer.net/forgot-password");
        Thread.sleep(3000);
        resetPage.submitEmail(uniqueUser);

        // --- STEP 4: GMAIL RETRIEVAL ---
        Thread.sleep(25000);
        String emailBody = EmailUtils.getResetLink("noorejannat19@gmail.com", "zrstzmvxjhvqlzjc");
        Assert.assertNotNull(emailBody, "Reset email not found in Inbox!");

        String resetLink = "http" + emailBody.split("http")[1].split(" ")[0].replace("\"", "").replace(">", "").trim();
        driver.get(resetLink);

        // --- STEP 5: NEW PASSWORD & FORCED LOGIN ---
        newPassPage.setNewPassword("NewPassword123!");
        Thread.sleep(4000);
        driver.get("https://dailyfinance.roadtocareer.net/login");

        wait.until(ExpectedConditions.urlContains("/login"));
        loginPage.login(uniqueUser, "NewPassword123!");

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("user"),
                ExpectedConditions.urlContains("dashboard")
        ));

        // --- STEP 6: ADD 2 ITEMS & VERIFY ---
        itemPage.addItem("Laptop", "55000", "Office stuff", true);
        Assert.assertTrue(itemPage.isItemVisible("Laptop"), "Laptop not found!");

        itemPage.addItem("Internet", "1000", "", false);
        Assert.assertTrue(itemPage.isItemVisible("Internet"), "Internet not found!");

        // --- STEP 7 & 8: PROFILE UPDATE ---
        Thread.sleep(1000);
        profilePage.navigateToProfile();

        String updatedEmail = "newuser" + System.currentTimeMillis() + "@gmail.com";
        profilePage.updateEmail(updatedEmail);
        profilePage.logout();

        // Login with updated email to verify success
        driver.get("https://dailyfinance.roadtocareer.net/login");
        loginPage.login(updatedEmail, "NewPassword123!");
        wait.until(ExpectedConditions.urlContains("/user"));
        profilePage.logout();

        // --- STEP 9 & 10: ADMIN VERIFICATION ---
        driver.get("https://dailyfinance.roadtocareer.net/login");
        loginPage.login("admin@test.com", "admin123");
        wait.until(ExpectedConditions.urlContains("admin"));

        Assert.assertTrue(adminPage.searchAndVerifyUser(updatedEmail), "Updated email not in admin list!");
    }
}