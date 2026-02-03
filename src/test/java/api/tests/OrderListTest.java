package api.tests;

import api.client.OrderApi;
import api.utils.BaseTest;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest extends BaseTest {

    private final OrderApi orderApi = new OrderApi();

    @Test
    public void ordersListIsReturned() {
        orderApi.getOrders()
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
