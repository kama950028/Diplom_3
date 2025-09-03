package praktikum.tests.stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.github.javafaker.Faker;
import org.junit.*;
import org.junit.runner.RunWith;
import praktikum.tests.stellarburgers.BaseUiTest;
import praktikum.tests.stellarburgers.infra.BrowserSuite;
import praktikum.tests.stellarburgers.infra.Browsers;
import praktikum.tests.stellarburgers.pages.*;
import praktikum.tests.stellarburgers.model.User;

import java.util.Locale;

import static io.restassured.RestAssured.given;
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

    static class LoginRequest {
        public String email;
        public String password;
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @BeforeClass
    public static void setupApi() {
        RestAssured.baseURI = BASE_URL;
    }

    @Before
    public void createUserViaApi() {
        this.email = faker.internet().emailAddress();
        this.password = "P" + faker.internet().password(7, 12);
        String name = faker.name().firstName();

        User user = new User(email, password, name);
        given().header("Content-Type", "application/json").body(user).when().post("/api/auth/register").then().statusCode(SC_OK);

        Response login = given().header("Content-Type", "application/json").body(new LoginRequest(email, password)).when().post("/api/auth/login").then().statusCode(SC_OK).extract().response();
        this.accessToken = login.path("accessToken");
    }

    @After
    public void deleteUserViaApi() {
        if (accessToken == null) return;
        String authHeader = accessToken.startsWith("Bearer ") ? accessToken : ("Bearer " + accessToken);
        given().header("Authorization", authHeader).when().delete("/api/auth/user").then().statusCode(SC_ACCEPTED);
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
