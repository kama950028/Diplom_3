package praktikum.tests.stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.Test;
import org.junit.runner.RunWith;
import praktikum.tests.stellarburgers.BaseUiTest;
import praktikum.tests.stellarburgers.infra.BrowserSuite;
import praktikum.tests.stellarburgers.infra.Browsers;
import praktikum.tests.stellarburgers.pages.*;
import praktikum.tests.stellarburgers.util.Data;

import static org.junit.Assert.assertTrue;

@RunWith(BrowserSuite.class)
@Browsers({"chrome", "yandex"})
public class RegisterTests extends BaseUiTest {

    @Test
    @Story("Регистрация: успешная")
    @Description("Регистрируемся с валидными полями (Faker)")
    public void successRegistrationTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();

        LoginPage login = new LoginPage(driver);
        login.goRegister();

        RegisterPage reg = new RegisterPage(driver);
        reg.register(Data.name(), Data.email(), Data.pass());

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
        reg.register("Auto Tester", Data.email(), "123"); // специально короткий
        assertTrue("Ожидали подсветку/текст ошибки пароля", reg.isPasswordErrorShown());
    }
}
