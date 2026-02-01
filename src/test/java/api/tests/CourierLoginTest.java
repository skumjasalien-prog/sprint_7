package api.tests;

import api.client.CourierApi;
import api.model.Courier;
import api.model.LoginRequest;
import api.utils.BaseTest;
import org.junit.Test;

public class CourierLoginTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();

    @Test
    public void courierCanLogin() {
        Courier courier = new Courier("login_test", "1234", "Liz");
        courierApi.createCourier(courier);

        courierApi.login(new LoginRequest("login_test", "1234"))
                .then()
                .statusCode(200);
    }
}
