package site.nomoreparties.stellarburgers.config;

public class Endpoints {


    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    //Эндпойнты пользователя
    public static final String USER_REGISTER_ENDPOINT = "/api/auth/register";
    public static final String USER_LOGIN_ENDPOINT = "/api/auth/login";
    public static final String USER_INFO_ENDPOINT = "/api/auth/user";
    public static final String USER_DELETE_ENDPOINT = "/api/auth/user";

    // Эндпоинты для заказов
    public static final String ORDER_CREATION_ENDPOINT = "/api/orders";
    public static final String USER_ORDERS_ENDPOINT = "/api/orders";

    // Эндпоинт для получения ингредиентов (вспомогательный)
    public static final String INGREDIENTS_ENDPOINT = "/api/ingredients";
}
