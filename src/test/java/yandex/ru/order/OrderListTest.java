package yandex.ru.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yandex.ru.clients.OrderClients;
import yandex.ru.clients.UserClients;
import yandex.ru.model.Order;
import yandex.ru.model.User;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class OrderListTest {
    User user;
    UserClients userClient;
    Order order;
    OrderClients orderClient;
    private static final String MESSAGE_ERROR_UNAUTHORIZED = "You should be authorised";

    @Before
    public void setUp() {
        user = User.generateRandomUser();
        userClient = new UserClients();
        ValidatableResponse response = userClient.createUser(user);
        user.setAllToken(response);
        order = Order.generateRandomBurger();
        orderClient = new OrderClients();
    }

    @After
    public void tearDown() {
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Getting a list of orders of an authorized user")
    public void getListOfOrdersOfAuthorizedUser() {
        userClient.loginUser(user);

        ValidatableResponse orderResponse = orderClient.getAllUserOrders(user);

        orderResponse.assertThat().statusCode(SC_OK);
        orderResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Getting a list of orders of non-authorized user")
    public void getListOfOrdersOfNonAuthorizedUser() {
        user.setAccessToken("");

        ValidatableResponse orderResponse = orderClient.getAllUserOrders(user);

        orderResponse.assertThat().statusCode(SC_UNAUTHORIZED);
        orderResponse.assertThat().body("success", equalTo(false));
        orderResponse.assertThat().body("message", equalTo(MESSAGE_ERROR_UNAUTHORIZED));
    }

}
