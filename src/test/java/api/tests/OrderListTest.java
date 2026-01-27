package api.tests;

import api.utils.BaseTest;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderListTest extends BaseTest {

    @Test
    public void canGetListOfOrders() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", not(empty()))
                .body("orders[0].track", notNullValue())
                .body("orders[0].firstName", notNullValue())
                .body("orders[0].lastName", notNullValue());
    }
}
