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
        return given()
                .spec(getBaseSpecification())
                .body(user)
                .when()
                .post(EndPoints.CREATE_USER)
                .then();
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
                    .delete(EndPoints.UPDATE_DELETE_USER)
                    .then();
        }
    }

    @Step("Update user information after authorisation")
    public ValidatableResponse updateUser(User currentUser, User newUser) {
        if (currentUser.getAccessToken() != null) {
            return given()
                    .spec(getBaseSpecification())
                    .header("Authorization", currentUser.getAccessToken())
                    .when()
                    .body(newUser)
                    .patch(EndPoints.UPDATE_DELETE_USER)
                    .then();
        } else {
            return given()
                    .spec(getBaseSpecification())
                    .when()
                    .body(newUser)
                    .patch(EndPoints.UPDATE_DELETE_USER)
                    .then();
        }
    }
}
