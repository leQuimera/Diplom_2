package yandex.ru.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import yandex.ru.model.Order;
import yandex.ru.model.User;
import yandex.ru.units.EndPoints;
import yandex.ru.units.RestAssuredClient;

import static io.restassured.RestAssured.given;

public class OrderClients extends RestAssuredClient {

    @Step("Create an order")
    public ValidatableResponse createOrder(User user, Order order) {
        return given()
                .spec(getBaseSpecification())
                .header("Authorization", user.getAccessToken())
                .when()
                .body(order)
                .post(EndPoints.CREATE_GET_ORDER)
                .then();
    }

    @Step("Get the list of client orders")
    public ValidatableResponse getAllUserOrders(User user) {
        return given()
                .spec(getBaseSpecification())
                .header("Authorization", user.getAccessToken())
                .when()
                .get(EndPoints.CREATE_GET_ORDER)
                .then();
    }
}
