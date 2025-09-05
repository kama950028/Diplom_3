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

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertTrue;

@RunWith(BrowserSuite.class)
@Browsers({"chrome", "yandex"})
@DisplayName("UI-тесты регистрации пользователя")
public class RegisterTests extends BaseUiTest {

    private final Faker faker = new Faker(Locale.ENGLISH);
    private String lastEmail;
    private String lastPassword;

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

    @After
    public void cleanupUser() {
        if (lastEmail == null || lastPassword == null) return;
        Response login = given().header("Content-Type", "application/json").body(new LoginRequest(lastEmail, lastPassword)).when().post("/api/auth/login");
        if (login.statusCode() == SC_OK) {
            String token = login.path("accessToken");
            String authHeader = token != null && token.startsWith("Bearer ") ? token : ("Bearer " + token);
            given().header("Authorization", authHeader).when().delete("/api/auth/user").then().statusCode(SC_ACCEPTED);
        }
        lastEmail = null;
        lastPassword = null;
    }

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    @Story("Регистрация: успешная")
    @Description("Регистрация с валидными данными и последующая проверка входа в профиль")
    public void successRegistrationTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();
        LoginPage login = new LoginPage(driver);
        login.goRegister();
        RegisterPage reg = new RegisterPage(driver);
        String email = faker.internet().emailAddress();
        String pass = "P" + faker.internet().password(7, 12);
        lastEmail = email;
        lastPassword = pass;
        reg.register(faker.name().firstName(), email, pass);
        main.clickProfile();
        assertTrue(new ProfilePage(driver).isOpened());
    }

    @Test
    @DisplayName("Ошибка регистрации при коротком пароле")
    @Story("Регистрация: ошибка при некорректном пароле (< 6 символов)")
    public void weakPasswordRegistrationTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.clickLoginMain();
        LoginPage login = new LoginPage(driver);
        login.goRegister();
        RegisterPage reg = new RegisterPage(driver);
        reg.register(faker.name().firstName(), faker.internet().emailAddress(), "123");
        assertTrue("Ожидали сообщение об ошибке пароля", reg.isPasswordErrorShown());
    }
}
