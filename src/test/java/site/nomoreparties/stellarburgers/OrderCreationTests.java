package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.assertations.OrderAssertions;
import site.nomoreparties.stellarburgers.client.OrderApiClient;
import site.nomoreparties.stellarburgers.client.UserApiClient;
import site.nomoreparties.stellarburgers.model.Ingredients;
import site.nomoreparties.stellarburgers.model.User;

import java.util.List;

@DisplayName("Тесты на создание заказа")
public class OrderCreationTests {
    private final UserApiClient userApiClient = new UserApiClient();
    private final OrderApiClient orderApiClient = new OrderApiClient();
    private final Faker faker = new Faker();
    private User user;
    private Ingredients ingredients;
    private String accessToken;
    List<String> invalidIngredients = List.of("invalid_ingredient_id");

    private String email = faker.internet().emailAddress();
    private String password = faker.internet().password();
    private String name = faker.name().firstName();

    @Before
    public void setUp() {
        user = new User(email, password, name);
        Response responseWithToken = userApiClient.createUser(user);
        accessToken = userApiClient.getAccessToken(responseWithToken);
    }

    //Тесты с авторизацией
    @Test
    @DisplayName("Создание заказа с авторизацией и валидными ингредиентами")
    @Description("Проверяет, что авторизованный пользователь может создать заказ с валидными ингредиентами")
    public void createOrderWithAuthReturns200() {
        ingredients = new Ingredients(orderApiClient.getRandomIngredientIds(3));
        Response response = orderApiClient.createOrder(ingredients, accessToken);
        OrderAssertions.assertOrderCreated(response);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, но без ингредиентов возвращает 400")
    @Description("Проверяет, что авторизованный пользователь не может создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsReturns400() {
        ingredients = new Ingredients(null);
        Response response = orderApiClient.createOrder(ingredients, accessToken);
        OrderAssertions.assertIngredientsMissingError(response);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и невалидным ингредиентом возвращает 500")
    @Description("Проверяет, что сервер возвращает 500, если передан несуществующий id ингредиента")
    public void createOrderWithInvalidIngredientsReturns500() {
        ingredients = new Ingredients(invalidIngredients);
        Response response = orderApiClient.createOrder(ingredients, accessToken);
        OrderAssertions.assertInvalidIngredientError(response);
    }

    // Тесты без авторизации
    @Test
    @DisplayName("Создание заказа без авторизации, но с ингредиентами")
    @Description("Проверяет, что неавторизованный пользователь может создать заказ с валидными ингредиентами")
    public void createOrderWithoutAuthWithIngredientsReturns200() {
        ingredients = new Ingredients(orderApiClient.getRandomIngredientIds(5));
        Response response = orderApiClient.createOrder(ingredients, null);
        OrderAssertions.assertOrderCreated(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов возвращает 400")
    @Description("Проверяет, что неавторизованный пользователь не может создать заказ без ингредиентов")
    public void createOrderWithoutAuthAndWithoutIngredientsReturns400() {
        ingredients = new Ingredients(null);
        Response response = orderApiClient.createOrder(ingredients, null);
        OrderAssertions.assertIngredientsMissingError(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с невалидным ингредиентом возвращает 500")
    @Description("Проверяет, что сервер возвращает 500, если неавторизованный пользователь передаёт несуществующий id ингредиента")
    public void createOrderWithoutAuthAndInvalidIngredientReturns500() {
        ingredients = new Ingredients(invalidIngredients);
        Response response = orderApiClient.createOrder(ingredients, null);
        OrderAssertions.assertInvalidIngredientError(response);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userApiClient.deleteUser(accessToken);
        }
    }
}
