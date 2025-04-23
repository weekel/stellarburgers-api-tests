package site.nomoreparties.stellarburgers.assertations;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderAssertions {

    //Создание заказа
    @Step("Проверка успешного создания заказа")
    public static void assertOrderCreated(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Step("Проверка ошибки 400 — ингредиенты не переданы")
    public static void assertIngredientsMissingError(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверка ошибки 500 — невалидный id ингредиента")
    public static void assertInvalidIngredientError(Response response) {
        response.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    //Получить заказы конкретного пользователя
    @Step("Проверка успешного получения заказов пользователя")
    public static void assertUserOrdersReceived(Response response, int ordersCount) {
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("orders", notNullValue())
                .body("orders.size()", equalTo(ordersCount));
    }

    @Step("Проверка ошибки 401 при получении заказов без авторизации")
    public static void assertGetOrdersUnauthorizedError(Response response) {
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
