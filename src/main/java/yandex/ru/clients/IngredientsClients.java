package yandex.ru.clients;

import io.restassured.response.ValidatableResponse;
import yandex.ru.units.EndPoints;
import yandex.ru.units.RestAssuredClient;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class IngredientsClients extends RestAssuredClient {

    public ValidatableResponse getAllListOfIngredients() {
        return given()
                .spec(getBaseSpecification())
                .when()
                .get(EndPoints.GET_INGREDIENTS)
                .then()
                .statusCode(SC_OK);
    }
}
