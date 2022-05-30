package yandex.ru.clients;

import org.apache.commons.lang3.RandomUtils;
import yandex.ru.units.EndPoints;
import yandex.ru.units.RestAssuredClient;

import static io.restassured.RestAssured.given;

public class OrderClients extends RestAssuredClient {

    public String getRandomHashOfIngredient() {
        int random = RandomUtils.nextInt(0, 14);
        return given()
                .when()
                .get(EndPoints.GET_INGREDIENTS)
                .then()
                .extract()
                .path("data[" + random + "]._id");
    }
}
