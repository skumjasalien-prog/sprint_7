package api.client;

import api.model.Order;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .post("/api/v1/orders");
    }

    @Step("Отмена заказа с track = {track}")
    public void cancelOrder(int track) {
        given()
                .put("/api/v1/orders/cancel?track=" + track);
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .get("/api/v1/orders");
    }
}
