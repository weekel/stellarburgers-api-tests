package site.nomoreparties.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.User;

import static io.restassured.RestAssured.given;
import static site.nomoreparties.stellarburgers.config.Endpoints.*;

public class UserApiClient extends BaseApiClient{

    @Step("Создание пользователя с email: {user.email}, name: {user.name}")
    public Response createUser(User user) {
        return sendPostRequest(USER_REGISTER_ENDPOINT, user);
    }

    @Step("Получение accessToken из ответа")
    public String getAccessToken(Response response) {
        return response.path("accessToken");
    }

    @Step("Удаление пользователя по accessToken")
    public void deleteUser(String accessToken){
        given()
                .spec(super.requestSpecification)
                .header("Authorization", accessToken)
                .when()
                .delete(USER_DELETE_ENDPOINT);
    }

    @Step("Авторизация пользователя с email: {user.email}")
    public Response loginUser(User user) {
        return sendPostRequest(USER_LOGIN_ENDPOINT, user);
    }

    @Step("Изменение данных пользователя")
    public Response updateUser(User user, String accessToken) {
        return given()
                .spec(super.requestSpecification)
                .header("Authorization", accessToken != null ? accessToken : "")
                .body(user)
                .when()
                .patch(USER_INFO_ENDPOINT);
    }
}
