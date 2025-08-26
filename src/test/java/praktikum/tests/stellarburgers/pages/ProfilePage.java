package praktikum.tests.stellarburgers.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By profileHeader = By.xpath("//*[text()='Профиль' or normalize-space(text())='Личный Кабинет']");
    private final By logoutBtn     = By.xpath("//button[normalize-space(text())='Выход']");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOpened() {
        return !driver.findElements(profileHeader).isEmpty();
    }

    @Step("Выходим из профиля")
    public void logout() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(logoutBtn));
        btn.click();
        // маркер, что мы «вышли»: появилась кнопка «Войти»
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[normalize-space(text())='Войти']")));
    }
}
