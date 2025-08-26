package praktikum.tests.stellarburgers.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By email     = By.xpath("//label[normalize-space(text())='Email']/following-sibling::input");
    private final By password  = By.xpath("//label[normalize-space(text())='Пароль']/following-sibling::input");
    private final By loginBtn  = By.xpath("//button[normalize-space(text())='Войти']");
    private final By registerLink = By.xpath("//a[normalize-space(text())='Зарегистрироваться']");
    private final By restoreLink  = By.xpath("//a[normalize-space(text())='Восстановить пароль']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(loginBtn),
                ExpectedConditions.visibilityOfElementLocated(registerLink)
        ));
    }

    @Step("Логинимся как {emailText}")
    public void login(String emailText, String passText) {
        WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(email));
        e.clear(); e.sendKeys(emailText);
        WebElement p = wait.until(ExpectedConditions.visibilityOfElementLocated(password));
        p.clear(); p.sendKeys(passText);
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
    }

    @Step("Переходим на форму регистрации")
    public void goRegister() { wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click(); }

    @Step("Переходим на форму восстановления пароля")
    public void goRestore()  { wait.until(ExpectedConditions.elementToBeClickable(restoreLink)).click(); }
}
