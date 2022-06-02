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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class OrderCreationTest {
    User user;
    UserClients userClient;
    Order order;
    OrderClients orderClient;
    private static final String MESSAGE_ERROR_BAD_REQUEST = "Ingredient ids must be provided";

    @Before
    public void setUp() {
        user = User.generateRandomUser();
        userClient = new UserClients();
        ValidatableResponse response = userClient.createUser(user);
        user.setAllToken(response);
        order = Order.createRandomBurger();
        orderClient = new OrderClients();
    }

    @After
    public void tearDown() {
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Create new order for authorized user")
    public void createNewOrderForAuthorizedUser() {
        userClient.loginUser(user);

        ValidatableResponse orderResponse = orderClient.createOrder(user, order);
        orderResponse.assertThat().statusCode(SC_OK);
        orderResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create new order for non-authorized user")
    public void createNewOrderForNonAuthorizedUser() {
        user.setAccessToken("");

        ValidatableResponse orderResponse = orderClient.createOrder(user, order);
        orderResponse.assertThat().statusCode(SC_OK);
        orderResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create new order without any ingredients for authorized user")
    public void createNewOrderWithoutIngredient() {

        ValidatableResponse orderResponse = orderClient.createOrder(user, Order.getBurgerWithoutIngredients());

        orderResponse.assertThat().statusCode(SC_BAD_REQUEST);
        orderResponse.assertThat().body("success", equalTo(false));
        orderResponse.assertThat().body("message", equalTo(MESSAGE_ERROR_BAD_REQUEST));
    }

    @Test
    @DisplayName("Create new order with non-valid ingredients for authorized user")
    public void createNewOrderWithNonValidIngredients() {
        ValidatableResponse orderResponse = orderClient.createOrder(user, Order.getBurgerWithIncorrectIngredients());

        orderResponse.assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

}
