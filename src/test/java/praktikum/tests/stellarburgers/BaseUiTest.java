package praktikum.tests.stellarburgers;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class BaseUiTest {

    protected static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    protected WebDriver driver;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get(BASE_URL);
    }

    @After
    public void tearDown() {
        try {
            if (driver != null) {
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Final screenshot: " + testName.getMethodName(), "image/png", new ByteArrayInputStream(png), ".png");
            }
        } catch (Exception ignored) {
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    protected void openMain() {
        driver.get(BASE_URL);
    }

    @Attachment(value = "{name}", type = "image/png")
    protected byte[] attachScreenshot(String name) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
