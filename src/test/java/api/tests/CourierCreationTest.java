package api.tests;

import api.model.Courier;
import api.model.LoginRequest;
import api.utils.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierCreationTest extends BaseTest {

    private Integer courierId;   // чтобы потом удалить курьера

    // ===== вспомогательные методы =====

    private void createCourier(Courier courier) {
        given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    private void loginAndSaveCourierId(String login, String password) {
        LoginRequest loginRequest = new LoginRequest(login, password);

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(loginRequest)
                        .when()
                        .post("/api/v1/courier/login");

        courierId = response.then().extract().path("id");
    }

    @After
    public void deleteCourier() {
        if (courierId != null) {
            given()
                    .when()
                    .delete("/api/v1/courier/" + courierId);
        }
    }

    // ===== ТЕСТЫ =====

    @Test
    public void courierCanBeCreated() {
        Courier courier = new Courier("liz_test_1", "1234", "Liz");

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        loginAndSaveCourierId("liz_test_1", "1234");
    }

    @Test
    public void cannotCreateTwoSameCouriers() {
        Courier courier = new Courier("liz_test_2", "1234", "Liz");

        createCourier(courier);

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then()
                .statusCode(409);

        loginAndSaveCourierId("liz_test_2", "1234");
    }

    @Test
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, "1234", "Liz");

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then()
                .statusCode(400);
    }

    @Test
    public void cannotCreateCourierWithoutPassword() {
        Courier courier = new Courier("liz_test_3", null, "Liz");

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then()
                .statusCode(400);
    }

    @Test
    public void cannotCreateCourierWithExistingLogin() {
        Courier courier = new Courier("liz_test_4", "1234", "Liz");

        createCourier(courier);

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");

        response.then()
                .statusCode(409);

        loginAndSaveCourierId("liz_test_4", "1234");
    }
}
