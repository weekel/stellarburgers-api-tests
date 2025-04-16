package site.nomoreparties.stellarburgers.assertations;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.User;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserAssertions {

    //Проверки регистрации пользователя

    @Step("Проверка успешного создания пользователя и возврата accessToken")
    public static void assertUserCreated(Response response, User expectedUser) {
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(expectedUser.getEmail()))
                .body("user.name", equalTo(expectedUser.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Step("Проверка, что пользователь уже существует")
    public static void assertUserAlreadyExistsError(Response response) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверка, что одно из обязательных полей не передано")
    public static void assertRequiredFieldsMissingError(Response response) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }


    //Проверки логина пользователя

    @Step("Проверка успешного логина пользователя и возврата accessToken")
    public static void assertUserLoginSuccessful(Response response, User expectedUser) {
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(expectedUser.getEmail()))
                .body("user.name", equalTo(expectedUser.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Step("Проверка, что логин с неверными данными возвращает 401 Unauthorized")
    public static void assertLoginUnauthorizedError(Response response) {
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }


    //Проверки изменения данных пользователя

    @Step("Проверка успешного обновления данных пользователя")
    public static void assertUserUpdated(Response response, User expectedUser) {
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        if (expectedUser.getEmail() != null) {
            response.then().body("user.email", equalTo(expectedUser.getEmail()));
        }

        if (expectedUser.getName() != null) {
            response.then().body("user.name", equalTo(expectedUser.getName()));
        }
    }

    @Step("Проверка ошибки 401 при попытке изменить данные без авторизации")
    public static void assertUpdateUnauthorizedError(Response response) {
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Проверка ошибки 403 при попытке изменить email на уже существующий")
    public static void assertEmailAlreadyExistsError(Response response) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }
}
