package api.tests;

import api.client.CourierApi;
import api.model.Courier;
import api.model.LoginRequest;
import api.utils.BaseTest;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreationTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();
    private Integer courierId;

    @After
    public void cleanUp() {
        if (courierId != null) {
            courierApi.deleteCourier(courierId);
        }
    }

    @Test
    public void courierCanBeCreated() {
        Courier courier = new Courier("liz_test", "1234", "Liz");

        courierApi.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        Response login = courierApi.login(new LoginRequest("liz_test", "1234"));
        courierId = login.then().extract().path("id");
    }
}
