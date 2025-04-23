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

@DisplayName("Тесты на получение заказов пользователя")
public class UserOrdersTests {
    private final UserApiClient userApiClient = new UserApiClient();
    private final OrderApiClient orderApiClient = new OrderApiClient();
    private final Faker faker = new Faker();

    private User user;
    private String accessToken;
    private Ingredients firstIngredients;
    private Ingredients secondIngredients;
    private int ordersCount = 2;

    private String email = faker.internet().emailAddress();
    private String password = faker.internet().password();
    private String name = faker.name().firstName();

    @Before
    public void setUp() {
        user = new User(email, password, name);
        Response responseWithToken = userApiClient.createUser(user);
        accessToken = userApiClient.getAccessToken(responseWithToken);

        firstIngredients = new Ingredients(orderApiClient.getRandomIngredientIds(3));
        secondIngredients = new Ingredients(orderApiClient.getRandomIngredientIds(2));
        orderApiClient.createOrder(firstIngredients, accessToken);
        orderApiClient.createOrder(secondIngredients, accessToken);
    }

    @Test
    @DisplayName("Авторизованный пользователь может получить свои заказы")
    @Description("Проверяет, что при наличии accessToken можно получить список заказов")
    public void getOrdersWithAuthReturns200() {
        Response response = orderApiClient.getUserOrders(accessToken);
        OrderAssertions.assertUserOrdersReceived(response, ordersCount);
    }

    @Test
    @DisplayName("Неавторизованный пользователь не может получить заказы")
    @Description("Проверяет, что без accessToken сервер возвращает ошибку 401")
    public void getOrdersWithoutAuthReturns401() {
        Response response = orderApiClient.getUserOrders(null);
        OrderAssertions.assertGetOrdersUnauthorizedError(response);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userApiClient.deleteUser(accessToken);
        }
    }
}
