package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import pages.utils.ConfigReader;
import pages.utils.EmailUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class FinanceTest extends BaseTest {

    @Test(priority = 1)
    public void testResetPasswordWithInvalidEmail() {
        ResetPasswordPage resetPage = new ResetPasswordPage(driver);
        driver.get("https://dailyfinance.roadtocareer.net/forgot-password");
        resetPage.submitEmail("thisdoesnotexist_xyz999@fakeemail.com");
        Assert.assertTrue(resetPage.isErrorMessageDisplayed(),
                "Expected error for non-existent email but none appeared.");
    }

    @Test(priority = 2)
    public void testResetPasswordWithEmptyEmail() {
        ResetPasswordPage resetPage = new ResetPasswordPage(driver);
        driver.get("https://dailyfinance.roadtocareer.net/forgot-password");
        resetPage.submitEmail("");
        boolean errorShown = resetPage.isErrorMessageDisplayed();
        boolean stayedOnPage = driver.getCurrentUrl().contains("forgot-password");
        Assert.assertTrue(errorShown || stayedOnPage,
                "Expected validation error for empty email but none appeared.");
    }

    @Test(priority = 3)
    public void testFullSystemFlow() throws Exception {

        LoginPage loginPage = new LoginPage(driver);
        RegistrationPage regPage = new RegistrationPage(driver);
        ResetPasswordPage resetPage = new ResetPasswordPage(driver);
        NewPasswordPage newPassPage = new NewPasswordPage(driver);
        ItemPage itemPage = new ItemPage(driver);
        ProfilePage profilePage = new ProfilePage(driver);
        AdminDashboardPage adminPage = new AdminDashboardPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // STEP 1: Register new user
        test.info("STEP 1: Registering new user");
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueUser = "noorejannat19+" + timestamp + "@gmail.com";
        loginPage.clickRegister();
        regPage.register("Noor", "Jannat", uniqueUser, "Password123!", "01712345678", "Dhaka");
        Assert.assertTrue(regPage.isCongratsMessageDisplayed(),
                "Registration failed!");
        test.pass("Registered: " + uniqueUser);

        // STEP 2-3: Send reset link
        test.info("STEP 2-3: Sending reset link");
        driver.get("https://dailyfinance.roadtocareer.net/forgot-password");
        Thread.sleep(2000);
        resetPage.submitEmail(uniqueUser);
        Thread.sleep(2000);
        test.pass("Reset link sent to: " + uniqueUser);

        // STEP 4: Get reset link from Gmail
        test.info("STEP 4: Waiting 25s for email then retrieving reset link");
        Thread.sleep(25000);
        String emailBody = EmailUtils.getResetLink(
                ConfigReader.get("GMAIL_USER"),
                ConfigReader.get("GMAIL_APP_PASSWORD")
        );
        Assert.assertNotNull(emailBody, "Reset email not received!");

        String resetLink = "";
        for (String part : emailBody.split("\\s+")) {
            if (part.startsWith("http") && part.contains("reset")) {
                resetLink = part.replace(">", "").replace("<", "").replace("\"", "").trim();
                break;
            }
        }
        Assert.assertFalse(resetLink.isEmpty(), "Reset link not found in email body!");
        driver.get(resetLink);
        Thread.sleep(3000);
        test.pass("Reset link opened: " + resetLink);

        // STEP 5: Set new password and login
        test.info("STEP 5: Setting new password");
        String newPassword = "NewPassword123!";
        newPassPage.setNewPassword(newPassword);
        Thread.sleep(4000);

        driver.get("https://dailyfinance.roadtocareer.net/login");
        wait.until(ExpectedConditions.urlContains("/login"));
        loginPage.login(uniqueUser, newPassword);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("user"),
                ExpectedConditions.urlContains("dashboard")
        ));
        Assert.assertTrue(
                driver.getCurrentUrl().contains("user") || driver.getCurrentUrl().contains("dashboard"),
                "Login with new password failed!"
        );
        test.pass("Login successful with new password");

        // STEP 6: Add 2 items
        test.info("STEP 6: Adding 2 items");
        itemPage.addItem("Laptop", "55000", "Office equipment", true);
        Assert.assertTrue(itemPage.isItemVisible("Laptop"), "Laptop not found in list!");
        test.pass("Item 1 added: Laptop");

        itemPage.addItem("Internet", "1000", "", false);
        Assert.assertTrue(itemPage.isItemVisible("Internet"), "Internet not found in list!");
        test.pass("Item 2 added: Internet");

        // STEP 7: Update profile email
        test.info("STEP 7: Updating profile email");
        Thread.sleep(1000);
        profilePage.navigateToProfile();
        String updatedEmail = "noorejannat19+updated" + timestamp + "@gmail.com";
        profilePage.updateEmail(updatedEmail);
        Thread.sleep(2000);
        test.pass("Email updated to: " + updatedEmail);

        // STEP 8: Assert new email works, old email fails
        test.info("STEP 8: Verifying new email login succeeds and old email fails");
        profilePage.logout();
        Thread.sleep(1000);

        driver.get("https://dailyfinance.roadtocareer.net/login");
        loginPage.login(updatedEmail, newPassword);
        wait.until(ExpectedConditions.urlContains("/user"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/user"),
                "Login with new email failed!");
        test.pass("New email login succeeded");
        profilePage.logout();
        Thread.sleep(1000);

        driver.get("https://dailyfinance.roadtocareer.net/login");
        loginPage.login(uniqueUser, newPassword);
        Thread.sleep(3000);
        Assert.assertTrue(loginPage.isLoginErrorDisplayed(),
                "Old email should have failed but succeeded!");
        test.pass("Old email login correctly failed");

        // STEP 9: Admin login from config (securely)
        test.info("STEP 9: Admin login");
        String adminEmail = ConfigReader.get("ADMIN_EMAIL");
        String adminPassword = ConfigReader.get("ADMIN_PASSWORD");
        Assert.assertNotNull(adminEmail, "ADMIN_EMAIL not in config!");
        Assert.assertNotNull(adminPassword, "ADMIN_PASSWORD not in config!");

        driver.get("https://dailyfinance.roadtocareer.net/login");
        loginPage.login(adminEmail, adminPassword);
        wait.until(ExpectedConditions.urlContains("admin"));
        test.pass("Admin login successful");

        // STEP 10: Verify updated email in admin dashboard
        test.info("STEP 10: Verifying updated email in admin dashboard");
        Assert.assertTrue(adminPage.searchAndVerifyUser(updatedEmail),
                "Updated email not found in admin dashboard!");
        test.pass("Updated email found in admin dashboard: " + updatedEmail);
    }
}