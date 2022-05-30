package yandex.ru.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import yandex.ru.model.User;
import yandex.ru.units.EndPoints;
import yandex.ru.units.RestAssuredClient;

import static io.restassured.RestAssured.given;

public class UserClients extends RestAssuredClient {
    @Step("Create new user")
    public ValidatableResponse createUser(User user) {
        ValidatableResponse response = given()
                .spec(getBaseSpecification())
                .body(user)
                .when()
                .post(EndPoints.CREATE_USER)
                .then();

        user.setAccessToken(response.extract().response().jsonPath().getString("accessToken"));
        return response;
    }

    @Step("User login")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpecification())
                .body(user)
                .when()
                .post(EndPoints.USER_LOGIN)
                .then();
    }

    @Step("Delete user")
    public void deleteUser(User user) {
        if (user.getAccessToken() != null) {
            given()
                    .spec(getBaseSpecification())
                    .header("Authorization", user.getAccessToken())
                    .when()
                    .delete(EndPoints.GET_UPDATE_USER)
                    .then();
        }
    }

    @Step("Delete user without authorisation")
    public ValidatableResponse deleteUserWithoutUserToken(User user) {
        return given()
                .spec(getBaseSpecification())
                .when()
                .delete(EndPoints.GET_UPDATE_USER)
                .then();
    }

    @Step("Update user information")
    public ValidatableResponse updateUser(User user) {
        return given()
                .spec(getBaseSpecification())
                .headers("Authorization", user.getAccessToken())
                .body(user)
                .when()
                .patch(EndPoints.GET_UPDATE_USER)
                .then();
    }

    @Step("Update user information without authorisation")
    public ValidatableResponse updateUserWithoutUserToken(User user) {
        return given()
                .spec(getBaseSpecification())
                .body(user)
                .when()
                .patch(EndPoints.GET_UPDATE_USER)
                .then();
    }
}
