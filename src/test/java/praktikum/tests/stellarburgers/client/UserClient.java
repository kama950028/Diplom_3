package praktikum.tests.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.tests.stellarburgers.model.LoginRequest;
import praktikum.tests.stellarburgers.model.User;

import static io.restassured.RestAssured.given;

public class UserClient {
    private final String baseUri;

    public UserClient(String baseUri) {
        this.baseUri = baseUri;
    }

    @Step("Регистрация пользователя {user.email}")
    public Response register(User user) {
        return given()
                .baseUri(baseUri)
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Логин пользователя {email}")
    public Response login(String email, String password) {
        return given()
                .baseUri(baseUri)
                .header("Content-Type", "application/json")
                .body(LoginRequest.of(email, password))
                .when()
                .post("/api/auth/login");
    }

    @Step("Удаление пользователя по accessToken")
    public Response delete(String accessToken) {
        String token = (accessToken != null && accessToken.startsWith("Bearer "))
                ? accessToken
                : "Bearer " + accessToken;
        return given()
                .baseUri(baseUri)
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user");
    }
}
