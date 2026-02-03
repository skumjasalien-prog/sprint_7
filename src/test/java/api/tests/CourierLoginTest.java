package api.tests;

import api.client.CourierApi;
import api.model.Courier;
import api.model.LoginRequest;
import api.utils.BaseTest;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();
    private Integer courierId;

    @After
    public void cleanUp() {
        if (courierId != null) {
            courierApi.deleteCourier(courierId);
        }
    }

    @Test
    public void courierCanLogin() {
        Courier courier = new Courier("login_test", "1234", "Liz");
        courierApi.createCourier(courier);

        Response loginResponse = courierApi.login(new LoginRequest("login_test", "1234"));

        loginResponse.then()
                .statusCode(200)
                .body("id", notNullValue());

        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    public void cannotLoginWithoutLogin() {
        courierApi.login(new LoginRequest(null, "1234"))
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void cannotLoginWithoutPassword() {
        courierApi.login(new LoginRequest("login_without_password", null))
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginWithWrongPasswordReturnsError() {
        Courier courier = new Courier("wrong_password_test", "1234", "Liz");
        courierApi.createCourier(courier);

        Response correctLogin = courierApi.login(new LoginRequest("wrong_password_test", "1234"));
        courierId = correctLogin.then().extract().path("id");

        courierApi.login(new LoginRequest("wrong_password_test", "wrong"))
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginNonExistingCourierReturnsError() {
        courierApi.login(new LoginRequest("non_existing_user", "1234"))
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
