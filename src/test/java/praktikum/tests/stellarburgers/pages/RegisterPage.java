package praktikum.tests.stellarburgers.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By name     = By.xpath("//label[normalize-space(text())='Имя']/following-sibling::input");
    private final By email    = By.xpath("//label[normalize-space(text())='Email']/following-sibling::input");
    private final By password = By.xpath("//label[normalize-space(text())='Пароль']/following-sibling::input");
    private final By registerButton = By.xpath("//button[normalize-space(text())='Зарегистрироваться']");
    private final By errorText = By.xpath("//*[contains(@class,'input__error') or contains(normalize-space(.),'Некорректный пароль')]");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(registerButton));
    }

    @Step("Регистрируем пользователя: {emailText}")
    public void register(String nameText, String emailText, String passText) {
        WebElement n = wait.until(ExpectedConditions.visibilityOfElementLocated(name));
        n.clear(); n.sendKeys(nameText);
        WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(email));
        e.clear(); e.sendKeys(emailText);
        WebElement p = wait.until(ExpectedConditions.visibilityOfElementLocated(password));
        p.clear(); p.sendKeys(passText);
        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();
    }

    public boolean isPasswordErrorShown() {
        return !driver.findElements(errorText).isEmpty();
    }
}
