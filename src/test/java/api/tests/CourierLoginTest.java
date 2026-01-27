package api.tests;

import api.model.Courier;
import api.model.LoginRequest;
import api.utils.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest extends BaseTest {

    private Integer courierId;

    private final String login = "liz_login_test";
    private final String password = "1234";
    private final String firstName = "Liz";

    @Before
    public void createCourierForLogin() {
        Courier courier = new Courier(login, password, firstName);

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then().statusCode(anyOf(is(201), is(409))); // если уже создан
    }

    @After
    public void deleteCourier() {
        if (courierId != null) {
            given()
                    .when()
                    .delete("/api/v1/courier/" + courierId);
        }
    }

    @Test
    public void courierCanLogin() {
        LoginRequest loginRequest = new LoginRequest(login, password);

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(loginRequest)
                        .when()
                        .post("/api/v1/courier/login");

        response.then()
                .statusCode(200)
                .body("id", notNullValue());

        courierId = response.then().extract().path("id");
    }

    @Test
    public void cannotLoginWithIncorrectLogin() {
        LoginRequest loginRequest = new LoginRequest("wrong_login", password);

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void cannotLoginWithIncorrectPassword() {
        LoginRequest loginRequest = new LoginRequest(login, "wrong_pass");

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void cannotLoginWithoutLoginField() {
        LoginRequest loginRequest = new LoginRequest(null, password);

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void cannotLoginWithoutPasswordField() {
        LoginRequest loginRequest = new LoginRequest(login, null);

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
