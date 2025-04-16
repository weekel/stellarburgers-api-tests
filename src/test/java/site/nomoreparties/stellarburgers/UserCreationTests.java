package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.assertations.UserAssertions;
import site.nomoreparties.stellarburgers.client.UserApiClient;
import site.nomoreparties.stellarburgers.model.User;

@DisplayName("Тесты на создание пользователя")
public class UserCreationTests {
    private final UserApiClient userApiClient = new UserApiClient();
    private final Faker faker = new Faker();
    private User user;
    private String accessToken;

    private String email = faker.internet().emailAddress();
    private String password = faker.internet().password();
    private String name = faker.name().firstName();

    @Before
    public void setUp() {
        user = new User(email, password, name);
    }

    @Test
    @DisplayName("Создание уникального пользователя возвращает 200 и токены")
    @Description("Проверяет, что при передаче валидных email, password и name пользователь создается успешно")
    public void createUserSuccessfullyReturns200AndTokens() {
        Response response = userApiClient.createUser(user);
        UserAssertions.assertUserCreated(response, user);
        accessToken = userApiClient.getAccessToken(response);
    }

    @Test
    @DisplayName("Повторная регистрация существующего пользователя возвращает 403")
    @Description("Проверяет, что при передеаче данных уже зарегестрированного пользователя возвращается ошибка ")
    public void createDuplicateUserReturns403() {
        Response responseWithToken = userApiClient.createUser(user);
        accessToken = userApiClient.getAccessToken(responseWithToken);

        Response response = userApiClient.createUser(user);
        UserAssertions.assertUserAlreadyExistsError(response);
    }

    @Test
    @DisplayName("Создание пользователя без email возвращает 403")
    @Description("Проверяет, что если не передан email, сервер возвращает ошибку")
    public void createUserWithoutEmailReturns403() {
        user.setEmail(null);

        Response response = userApiClient.createUser(user);
        UserAssertions.assertRequiredFieldsMissingError(response);
    }

    @Test
    @DisplayName("Создание пользователя без пароля возвращает 403")
    @Description("Проверяет, что если не передан password, сервер возвращает ошибку")
    public void createUserWithoutPasswordReturns403() {
        user.setPassword(null);

        Response response = userApiClient.createUser(user);
        UserAssertions.assertRequiredFieldsMissingError(response);
    }

    @Test
    @DisplayName("Создание пользователя без имени возвращает 403")
    @Description("Проверяет, что если не передан name, сервер возвращает ошибку")
    public void createUserWithoutNameReturns403() {
        user.setName(null);

        Response response = userApiClient.createUser(user);
        UserAssertions.assertRequiredFieldsMissingError(response);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userApiClient.deleteUser(accessToken);
        }
    }


}
