package yandex.ru.user.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yandex.ru.clients.UserClients;
import yandex.ru.model.User;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class UserAuthorisationTest {
    private User user;
    private UserClients userClients;
    private static final String MESSAGE_FORBIDDEN_DUPLICATE_USER = "User already exists";
    private static final String MESSAGE_FORBIDDEN_REQUIRED_FIELDS = "Email, password and name are required fields";

    @Before
    public void setUp() {
        user = User.createRandomUser();
        userClients = new UserClients();
    }

    @After
    public void tearDown() {
        userClients.deleteUser(user);
    }

    @Test
    @DisplayName("Successful creation of a user")
    public void userCanBeCreated() {
        ValidatableResponse response = userClients.createUser(user);
        user.setAllToken(response);
        response.assertThat().statusCode(SC_OK);
        response.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Recreation of a user")
    public void duplicateUserCannotBeCreated() {
        userClients.createUser(user);
        ValidatableResponse responseDuplicate = userClients.createUser(user);
        responseDuplicate.assertThat().statusCode(SC_FORBIDDEN);
        responseDuplicate.assertThat().body("success", equalTo(false));
        responseDuplicate.assertThat().body("message", equalTo(MESSAGE_FORBIDDEN_DUPLICATE_USER));
    }

    @Test
    @DisplayName("Trying new user without email")
    public void registrationUserWithoutEmail() {
        user.setEmail(null);
        ValidatableResponse response = userClients.createUser(user);
        response.assertThat().statusCode(SC_FORBIDDEN);
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo(MESSAGE_FORBIDDEN_REQUIRED_FIELDS));
    }

    @Test
    @DisplayName("Trying new user without email")
    public void registrationUserWithoutPassword() {
        user.setPassword(null);
        ValidatableResponse response = userClients.createUser(user);
        response.assertThat().statusCode(SC_FORBIDDEN);
        response.assertThat().body("success", equalTo(false));
        response.assertThat().body("message", equalTo(MESSAGE_FORBIDDEN_REQUIRED_FIELDS));
    }
}
