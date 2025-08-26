package praktikum.tests.stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.Test;
import org.junit.runner.RunWith;
import praktikum.tests.stellarburgers.BaseUiTest;
import praktikum.tests.stellarburgers.infra.BrowserSuite;
import praktikum.tests.stellarburgers.infra.Browsers;
import praktikum.tests.stellarburgers.pages.*;

import static org.junit.Assert.assertTrue;


@RunWith(BrowserSuite.class)
@Browsers({"chrome", "yandex"})
public class LoginTests extends BaseUiTest {

    private final String EXISTING_EMAIL = "test@example.com";
    private final String EXISTING_PASS  = "qwerty1";

    @Test
    @Story("Вход: кнопка «Войти в аккаунт» на главной")
    @Description("Переход с главной и успешный логин")
    public void loginFromMainTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();

        LoginPage login = new LoginPage(driver);
        login.login(EXISTING_EMAIL, EXISTING_PASS);

        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }

    @Test
    @Story("Вход: через «Личный кабинет»")
    public void loginFromProfileLinkTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickProfile();

        LoginPage login = new LoginPage(driver);
        login.login(EXISTING_EMAIL, EXISTING_PASS);

        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }

    @Test
    @Story("Вход: кнопка в форме регистрации")
    public void loginFromRegisterFormTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();
        LoginPage login = new LoginPage(driver);
        login.goRegister();

        // Вернуться на логин с формы регистрации ссылкой «Войти»
        driver.navigate().back();

        login.login(EXISTING_EMAIL, EXISTING_PASS);
        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }

    @Test
    @Story("Вход: кнопка в форме восстановления пароля")
    public void loginFromRestoreFormTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();

        LoginPage login = new LoginPage(driver);
        login.goRestore();

        RestorePage restore = new RestorePage(driver);
        restore.goLogin();

        login.login(EXISTING_EMAIL, EXISTING_PASS);
        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }
}
