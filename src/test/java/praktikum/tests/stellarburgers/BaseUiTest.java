package praktikum.tests.stellarburgers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public abstract class BaseUiTest {
    protected WebDriver driver;

    protected static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    @Before
    public void setUp() {
        String browser = System.getProperty("browser", "chrome").toLowerCase().trim();

        switch (browser) {
            case "yandex":
            case "ya":
                driver = createYandexDriver();
                break;
            case "chrome":
            default:
                driver = createChromeDriver();
        }

        driver.manage().window().maximize();
        // без неявных ожиданий, чтобы избежать конфликтов с явными
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        // зафиксируем окружение для Allure
        writeAllureEnv("Browser=" + browser);

        openMain();
    }

    @After
    public void tearDown() {
        try {
            if (driver != null) {
                // прикрепим финальный скрин (полезно при падениях)
                try {
                    byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    io.qameta.allure.Allure.addAttachment("Final screenshot", "image/png",
                            new java.io.ByteArrayInputStream(png), ".png");
                } catch (Exception ignored) {}
                driver.quit();
            }
        } catch (Exception ignored) {}
    }

    // -------------------- Drivers --------------------

    private WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = buildCommonChromeOptions();
        return new ChromeDriver(options);
    }

    private WebDriver createYandexDriver() {
        // 1) Определяем бинарник Я.Браузера
        String yaBin = System.getProperty("yandex.binary");
        if (yaBin == null || yaBin.isBlank()) {
            String home = System.getProperty("user.home");
            String win = home + "\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe";
            String mac = "/Applications/Yandex.app/Contents/MacOS/Yandex";
            String linux = "/usr/bin/yandex-browser";
            if (Files.exists(Path.of(win))) yaBin = win;
            else if (Files.exists(Path.of(mac))) yaBin = mac;
            else if (Files.exists(Path.of(linux))) yaBin = linux;
        }

        ChromeOptions options = buildCommonChromeOptions();
        if (yaBin != null) options.setBinary(yaBin);

        // 2) Подбираем совместимый chromedriver по major (по умолчанию 136)
        String yaMajor = System.getProperty("yandex.major", "136");
        WebDriverManager.chromedriver().browserVersion(yaMajor).setup();

        return new ChromeDriver(options);
    }

    private ChromeOptions buildCommonChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // стратегия загрузки страницы
        String pls = System.getProperty("pageLoadStrategy", "normal").toLowerCase().trim();
        switch (pls) {
            case "none":   options.setPageLoadStrategy(PageLoadStrategy.NONE); break;
            case "eager":  options.setPageLoadStrategy(PageLoadStrategy.EAGER); break;
            default:       options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        }

        // headless при необходимости
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--start-maximized");

        return options;
    }

    // -------------------- Helpers --------------------

    @Step("Открываем главную страницу")
    protected void openMain() {
        driver.get(BASE_URL);
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
        } catch (Exception ignored) {}
    }

    private void writeAllureEnv(String line) {
        try {
            Path dir = Path.of("target", "allure-results");
            Files.createDirectories(dir);
            Path env = dir.resolve("environment.properties");
            Files.write(env, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    Files.exists(env)
                            ? new java.nio.file.OpenOption[]{java.nio.file.StandardOpenOption.APPEND}
                            : new java.nio.file.OpenOption[]{java.nio.file.StandardOpenOption.CREATE});
        } catch (Exception ignored) {}
    }
}
