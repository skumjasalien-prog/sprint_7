package api.client;

import api.model.Courier;
import api.model.LoginRequest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .post("/api/v1/courier");
    }

    @Step("Логин курьера")
    public Response login(LoginRequest loginRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/api/v1/courier/login");
    }

    @Step("Удаление курьера")
    public void deleteCourier(int courierId) {
        given()
                .delete("/api/v1/courier/" + courierId);
    }
}
