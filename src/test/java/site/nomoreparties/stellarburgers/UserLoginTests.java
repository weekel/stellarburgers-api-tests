package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.assertations.UserAssertions;
import site.nomoreparties.stellarburgers.client.UserApiClient;
import site.nomoreparties.stellarburgers.model.User;

@RunWith(Enclosed.class)
public class UserLoginTests {

    private static final Faker faker = new Faker();

    @DisplayName("Тесты логина без параметров")
    public static class LoginPositiveTests {
        private final UserApiClient userApiClient = new UserApiClient();
        private User userToRegister;
        private User userToLogin;
        private String accessToken;

        private final String email = faker.internet().emailAddress();
        private final String password = faker.internet().password();
        private final String name = faker.name().firstName();

        @Before
        public void setUp(){
            userToRegister = new User(email, password, name);
            userToLogin = new User(email, password);
            Response responseWithToken = userApiClient.createUser(userToRegister);
            accessToken = userApiClient.getAccessToken(responseWithToken);
        }

        @Test
        @DisplayName("Успешная авторизация под существующим пользователем")
        @Description("Проверяет, что пользователь может войти с валидным email и паролем")
        public void loginWithValidCredentialsReturns200(){
            Response response = userApiClient.loginUser(userToLogin);
            UserAssertions.assertUserLoginSuccessful(response, userToRegister);
        }

        @After
        public void tearDown() {
            if (accessToken != null) {
                userApiClient.deleteUser(accessToken);
            }
        }
    }


    @RunWith(Parameterized.class)
    @DisplayName("Тесты логина параметризованные")
    public static class LoginParameterizedTests {
        private final UserApiClient userApiClient = new UserApiClient();
        private User userToRegister;
        private User userToLogin;
        private String accessToken;

        private static final String validEmail = faker.internet().emailAddress();
        private static final String validPassword = faker.internet().password();
        private final String validName = faker.name().firstName();

        private final String email;
        private final String password;

        public LoginParameterizedTests(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Parameterized.Parameters
        public static Object[][] getTestData() {
            return new Object[][]{
                {validEmail, "wrongPassword"},
                {"wrong_" + validEmail, validPassword},
                {null, validPassword},
                {validEmail, null},
                {"", ""}
            };
            }

        @Before
        public void setUp(){
            userToRegister = new User(validEmail, validPassword, validName);
            Response responseWithToken = userApiClient.createUser(userToRegister);
            accessToken = userApiClient.getAccessToken(responseWithToken);

            userToLogin = new User(email, password);
        }

        @Test
        @DisplayName("Логин с некорректными данными возвращает 401")
        @Description("Проверка, что логин с неверным email или паролем или их отсутствием возвращает 401 Unauthorized")
        public void loginWithIncorrectPasswordReturns401() {
            Response response = userApiClient.loginUser(userToLogin);
            UserAssertions.assertLoginUnauthorizedError(response);
        }
        @After
        public void tearDown() {
            if (accessToken != null) {
                userApiClient.deleteUser(accessToken);
            }
        }
        }
    }