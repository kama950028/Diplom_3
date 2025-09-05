package praktikum.tests.stellarburgers.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProfilePage {
    private final WebDriver driver;
    private final By profileHeader = By.xpath("//*[text()='Профиль' or normalize-space(text())='Личный Кабинет']");

    public ProfilePage(WebDriver driver) { this.driver = driver; }

    @Step("Страница входа открыта")
    public boolean isOpened() {
        return !driver.findElements(profileHeader).isEmpty();
    }
}
