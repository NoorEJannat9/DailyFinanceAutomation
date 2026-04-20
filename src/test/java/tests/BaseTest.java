package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {
    public WebDriver driver;

    @BeforeMethod
    public void setup() {
        // Automatically sets up the Chrome driver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        // Basic browser settings
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Opens the site URL from your project requirements
        driver.get("https://dailyfinance.roadtocareer.net/");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            // Closes the browser after each test
            driver.quit();
        }
    }
}