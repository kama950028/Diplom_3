package praktikum.tests.stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import praktikum.tests.stellarburgers.BaseUiTest;
import praktikum.tests.stellarburgers.infra.BrowserSuite;
import praktikum.tests.stellarburgers.infra.Browsers;
import praktikum.tests.stellarburgers.pages.*;

import static org.junit.Assert.assertTrue;


@RunWith(BrowserSuite.class)
@Browsers({"chrome", "yandex"})
public class RegisterTests extends BaseUiTest {

    private String uniqueEmail() {
        return "auto+" + System.currentTimeMillis() + "@example.com";
    }

    @Test
    @Story("Регистрация: успешная")
    @Description("Регистрация с валидным паролем (>= 6 символов) с развилкой: редирект на логин или сразу на главную")
    public void successRegistrationTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();

        LoginPage login = new LoginPage(driver);
        login.goRegister();

        RegisterPage reg = new RegisterPage(driver);
        String email = uniqueEmail();
        String pass = "qwerty1";
        reg.register("Auto Tester", email, pass);

        // Ждём ИЛИ логин-форму, ИЛИ уже главную (после авто-логина)
        boolean onLoginPage  = !driver.findElements(By.xpath("//button[normalize-space(text())='Войти']")).isEmpty();
        boolean onMainPage   = !driver.findElements(By.xpath("//p[normalize-space(text())='Личный Кабинет']")).isEmpty();

        if (onLoginPage) {
            new LoginPage(driver).login(email, pass);
        } else if (!onMainPage) {
            // подстраховка (короткое ожидание)
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            onLoginPage = !driver.findElements(By.xpath("//button[normalize-space(text())='Войти']")).isEmpty();
            if (onLoginPage) new LoginPage(driver).login(email, pass);
        }

        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }

    @Test
    @Story("Регистрация: ошибка при некорректном пароле (< 6 символов)")
    public void weakPasswordRegistrationTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();

        LoginPage login = new LoginPage(driver);
        login.goRegister();

        RegisterPage reg = new RegisterPage(driver);
        reg.register("Auto Tester", uniqueEmail(), "123"); // слишком короткий
        assertTrue("Ожидали подсветку/текст ошибки пароля", reg.isPasswordErrorShown());
    }
}
