package praktikum.tests.stellarburgers.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RestorePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By loginLink = By.xpath("//a[normalize-space(text())='Войти']");

    public RestorePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginLink));
    }

    @Step("Из восстановления пароля — на страницу логина")
    public void goLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }
}
