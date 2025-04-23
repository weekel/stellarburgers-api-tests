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
@DisplayName("Тесты на изменение данных пользователя")
public class UserUpdateTests {

    private static final Faker faker = new Faker();

    @RunWith(Parameterized.class)
    @DisplayName("Параметризованные тесты на изменение данных пользователя")
    public static class UpdateParameterizedTests {
        private final UserApiClient userApiClient = new UserApiClient();
        private User updatedUser;
        private String accessToken;

        private static final String validEmail = faker.internet().emailAddress();
        private static final String validPassword = faker.internet().password();
        private final String validName = faker.name().firstName();

        private final String email;
        private final String name;

        public UpdateParameterizedTests(String email, String name) {
            this.email = email;
            this.name = name;
        }

        @Parameterized.Parameters
        public static Object[][] getTestData() {
            return new Object[][]{
                    {"new1@user.com", "New Name 1"},   // оба поля
                    {null, "Only Name"},                  // только имя
                    {"only@email.com", null}             // только email
            };
        }

        @Before
        public void setUp() {
            User user = new User(validEmail, validPassword, validName);
            Response responseWithToken = userApiClient.createUser(user);
            accessToken = userApiClient.getAccessToken(responseWithToken);

            updatedUser = new User(email, null, name);
        }

        @Test
        @DisplayName("Обновление данных с авторизацией")
        @Description("Проверяет, что email и/или name обновляются при наличии accessToken")
        public void updateUserWithAuthReturns200() {
            Response response = userApiClient.updateUser(updatedUser, accessToken);
            UserAssertions.assertUserUpdated(response, updatedUser);
        }

        @Test
        @DisplayName("Попытка изменить данные без авторизации возвращает 401")
        @Description("Проверяет, что при отсутствии accessToken PATCH-запрос возвращает ошибку авторизации")
        public void updateUserWithoutAuthReturns401() {
            Response response = userApiClient.updateUser(updatedUser, null);
            UserAssertions.assertUpdateUnauthorizedError(response);
        }


        @After
        public void tearDown() {
            if (accessToken != null) {
                userApiClient.deleteUser(accessToken);
            }
        }
    }


    @DisplayName("Тесты без параметров на изменение данных пользователя")
    public static class NonParameterizedUpdateTests {

        private final UserApiClient userApiClient = new UserApiClient();

        private User firstUser;
        private User secondUser;
        private User updatedUser;
        private String firstUserToken;
        private String secondUserToken;

        private final String firstUserEmail = faker.internet().emailAddress();
        private final String secondUserEmail = faker.internet().emailAddress();
        private final String password = faker.internet().password();
        private final String name = faker.name().firstName();

        @Before
        public void setUp(){
            firstUser = new User(firstUserEmail, password, name);
            Response responseWithToken1 = userApiClient.createUser(firstUser);
            firstUserToken = userApiClient.getAccessToken(responseWithToken1);

            secondUser = new User(secondUserEmail, password, name);
            Response responseWithToken2 = userApiClient.createUser(secondUser);
            secondUserToken = userApiClient.getAccessToken(responseWithToken2);
        }

        @Test
        @DisplayName("Изменение email на уже существующий возвращает 403")
        @Description("Проверка, что при попытке изменить email на уже занятый возвращается ошибка 403 Forbidden")
        public void updateUserWithDuplicateEmailReturns403() {
            updatedUser = new User(firstUserEmail, null, null);
            Response response = userApiClient.updateUser(updatedUser, secondUserToken);
            UserAssertions.assertEmailAlreadyExistsError(response);
        }

        @After
        public void tearDown() {
            if (firstUserToken != null) userApiClient.deleteUser(firstUserToken);
            if (secondUserToken != null) userApiClient.deleteUser(secondUserToken);
        }
    }
}

