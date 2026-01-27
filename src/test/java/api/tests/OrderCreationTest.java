package api.tests;

import api.model.Order;
import api.utils.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderCreationTest extends BaseTest {

    @Test
    public void orderCanBeCreatedWithAllFields() {
        Order order = new Order(
                "Liz",
                "Smith",
                "Nevsky 10",
                "5",
                "+79991234567",
                3,
                "2026-01-27",
                "Please be on time",
                Arrays.asList("BLACK", "GREY")
        );

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(order)
                        .when()
                        .post("/api/v1/orders");

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Test
    public void orderCanBeCreatedWithoutColor() {
        Order order = new Order(
                "Liz",
                "Smith",
                "Nevsky 10",
                "5",
                "+79991234567",
                2,
                "2026-01-27",
                "No color preference",
                null // цвет не указан
        );

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(order)
                        .when()
                        .post("/api/v1/orders");

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Test
    public void cannotCreateOrderWithoutFirstName() {
        Order order = new Order(
                null, // нет имени
                "Smith",
                "Nevsky 10",
                "5",
                "+79991234567",
                2,
                "2026-01-27",
                "Missing first name",
                Collections.singletonList("BLACK")
        );

        given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для заказа"));
    }

    @Test
    public void cannotCreateOrderWithoutAddress() {
        Order order = new Order(
                "Liz",
                "Smith",
                null, // нет адреса
                "5",
                "+79991234567",
                2,
                "2026-01-27",
                "Missing address",
                Collections.singletonList("BLACK")
        );

        given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для заказа"));
    }
}
