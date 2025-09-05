package praktikum.tests.stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import com.github.javafaker.Faker;
import org.junit.*;
import org.junit.runner.RunWith;
import praktikum.tests.stellarburgers.BaseUiTest;
import praktikum.tests.stellarburgers.infra.BrowserSuite;
import praktikum.tests.stellarburgers.infra.Browsers;
import praktikum.tests.stellarburgers.pages.*;
import praktikum.tests.stellarburgers.model.User;
import praktikum.tests.stellarburgers.client.UserClient;

import java.util.Locale;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertTrue;

@RunWith(BrowserSuite.class)
@Browsers({"chrome", "yandex"})
@DisplayName("UI-тесты входа в аккаунт")
public class LoginTests extends BaseUiTest {

    private final Faker faker = new Faker(Locale.ENGLISH);
    private String email;
    private String password;
    private String accessToken;
    private UserClient userClient;

    @BeforeClass
    public static void setupApiBase() {
    }

    @Before
    public void createUserViaApi() {
        this.userClient = new UserClient(BASE_URL);

        this.email = faker.internet().emailAddress();
        this.password = "P" + faker.internet().password(7, 12);
        String name = faker.name().firstName();

        User user = new User(email, password, name);

        userClient.register(user)
                .then()
                .statusCode(SC_OK);

        this.accessToken = userClient.login(email, password)
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
    }

    @After
    public void deleteUserViaApi() {
        if (accessToken == null) return;
        userClient.delete(accessToken)
                .then()
                .statusCode(SC_ACCEPTED);
    }

    @Test
    @DisplayName("Вход через кнопку «Войти в аккаунт» на главной")
    @Story("Вход: кнопка «Войти в аккаунт» на главной")
    @Description("Проверка логина с главной страницы")
    public void loginFromMainTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();
        new LoginPage(driver).login(email, password);
        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }

    @Test
    @DisplayName("Вход через ссылку «Личный кабинет»")
    @Story("Вход: через «Личный кабинет»")
    public void loginFromProfileLinkTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickProfile();
        new LoginPage(driver).login(email, password);
        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }

    @Test
    @DisplayName("Вход со страницы регистрации")
    @Story("Вход: кнопка в форме регистрации")
    public void loginFromRegisterFormTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();
        LoginPage login = new LoginPage(driver);
        login.goRegister();
        driver.navigate().back();
        login.login(email, password);
        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }

    @Test
    @DisplayName("Вход со страницы восстановления пароля")
    @Story("Вход: кнопка в форме восстановления пароля")
    public void loginFromRestoreFormTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();
        LoginPage login = new LoginPage(driver);
        login.goRestore();
        new RestorePage(driver).goLogin();
        login.login(email, password);
        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }
}
