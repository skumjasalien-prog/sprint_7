package api.tests;

import api.client.OrderApi;
import api.model.Order;
import api.utils.BaseTest;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest extends BaseTest {

    private final OrderApi orderApi = new OrderApi();
    private final List<String> colors;
    private Integer track;

    public OrderCreationTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвета: {0}")
    public static Object[][] data() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {null}
        };
    }

    @After
    public void cancelOrderAfterTest() {
        if (track != null) {
            orderApi.cancelOrder(track);
        }
    }

    @Test
    public void orderCanBeCreatedWithDifferentColors() {
        Order order = new Order(
                "Liz",
                "Test",
                "Street 1",
                "1",
                "+79999999999",
                3,
                "2026-01-01",
                "comment",
                colors
        );

        Response response = orderApi.createOrder(order);

        track = response.then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract()
                .path("track");
    }
}
