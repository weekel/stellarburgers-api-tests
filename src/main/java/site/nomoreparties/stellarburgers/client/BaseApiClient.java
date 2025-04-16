package site.nomoreparties.stellarburgers.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static site.nomoreparties.stellarburgers.config.Endpoints.BASE_URL;

public class BaseApiClient {

    protected RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .addHeader("Content-type", "application/json")
            .addHeader("requestType","autotest")
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .build();

    protected Response sendGetRequest(String endpoint) {
        return given()
                .spec(requestSpecification)
                .when()
                .get(endpoint);
    }

    protected Response sendPostRequest(String endpoint, Object body) {
        return given()
                .spec(requestSpecification)
                .body(body)
                .when()
                .post(endpoint);
    }

    protected Response sendDeleteRequest(String endpoint) {
        return given()
                .spec(requestSpecification)
                .when()
                .delete(endpoint);
    }
}
