package site.nomoreparties.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.Ingredients;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static site.nomoreparties.stellarburgers.config.Endpoints.*;

public class OrderApiClient extends BaseApiClient{

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return sendGetRequest(INGREDIENTS_ENDPOINT);
    }

    @Step("Извлечение списка id всех ингредиентов")
    public List<String> getIngredientsIdList() {
        Response response = getIngredients();
        return response.jsonPath().getList("data._id");
    }

    @Step("Получение случайных {count} id ингредиентов")
    public List<String> getRandomIngredientIds(int count) {
        List<String> all =getIngredientsIdList();
        Collections.shuffle(all);
        return all.subList(0, Math.min(count, all.size()));
    }

    @Step("Создание заказа с ингредиентами: {ingredients}")
    public Response createOrder(Ingredients ingredients, String accessToken) {
        return given()
                .spec(super.requestSpecification)
                .header("Authorization", accessToken != null ? accessToken : "")
                .body(ingredients)
                .when()
                .post(ORDER_CREATION_ENDPOINT);
    }

    @Step("Получение заказов пользователя")
    public Response getUserOrders(String accessToken) {
        return given()
                .spec(super.requestSpecification)
                .header("Authorization", accessToken != null ? accessToken : "")
                .when()
                .get(USER_ORDERS_ENDPOINT);
    }
}
