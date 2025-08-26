package praktikum.tests.stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.Before;
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
public class LoginTests extends BaseUiTest {

    private String email;
    private String pass;
    private String name;

    @Before
    public void createUserViaUI() {
        // генерим данные Faker'ом
        name  = Data.name();
        email = Data.email();
        pass  = Data.pass();

        // Регистрируем через UI
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();

        LoginPage login = new LoginPage(driver);
        login.goRegister();

        RegisterPage reg = new RegisterPage(driver);
        reg.register(name, email, pass);

        // убеждаемся, что зашли, и выходим — чтобы тестировать сценарии входа
        main.clickProfile();
        ProfilePage profile = new ProfilePage(driver);
        assertTrue("Профиль не открылся после регистрации", profile.isOpened());
        profile.logout();

        // вернёмся на главную для чистоты
        openMain();
    }

    @Test
    @Story("Вход: кнопка «Войти в аккаунт» на главной")
    @Description("Проверяем логин с главной страницы по кнопке «Войти в аккаунт»")
    public void loginFromMainButtonTest() {
        MainPage main = new MainPage(driver);
        main.clickLoginMain();

        LoginPage login = new LoginPage(driver);
        login.login(email, pass);

        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }

    @Test
    @Story("Вход: через «Личный Кабинет»")
    public void loginFromProfileLinkTest() {
        MainPage main = new MainPage(driver);
        main.clickProfile(); // в неавторизованном состоянии ведёт на форму логина

        LoginPage login = new LoginPage(driver);
        login.login(email, pass);

        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }

    @Test
    @Story("Вход: через «Восстановление пароля» -> «Войти»")
    public void loginFromRestoreFlowTest() {
        MainPage main = new MainPage(driver);
        main.clickLoginMain();

        LoginPage login = new LoginPage(driver);
        login.goRestore();

        RestorePage restore = new RestorePage(driver);
        restore.goLogin();

        login.login(email, pass);
        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }
}
