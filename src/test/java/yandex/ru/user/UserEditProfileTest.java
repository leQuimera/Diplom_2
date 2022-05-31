package yandex.ru.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yandex.ru.clients.UserClients;
import yandex.ru.model.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserEditProfileTest {
    private User currentUser;
    private User newUser;
    private UserClients userClients;
    private static final String MESSAGE_ERROR_UNAUTHORIZED = "You should be authorised";
    private static final String MESSAGE_ERROR_FORBIDDEN = "User with such email already exists";

    @Before
    public void setUp() {
        currentUser = User.createRandomUser();
        newUser = User.createRandomUser();
        userClients = new UserClients();
    }

    @After
    public void tearDown() {
        userClients.deleteUser(currentUser);
    }

    @Test
    @DisplayName("Change all information about user")
    public void changeAllInformationForAuthorizedUser() {
        ValidatableResponse response = userClients.createUser(currentUser);
        currentUser.setAllToken(response);

        ValidatableResponse responseUpdate = userClients.updateUser(currentUser, newUser);
        responseUpdate.assertThat().statusCode(SC_OK);
        responseUpdate.assertThat().body("user.email", equalTo(newUser.getEmail()));
        responseUpdate.assertThat().body("user.name", equalTo(newUser.getName()));
    }

    @Test
    @DisplayName("Trying to change non authorized user")
    public void tryToChangeNonAuthorizedUser() {
        userClients.createUser(currentUser);

        ValidatableResponse responseUpdate = userClients.updateUser(currentUser, newUser);
        responseUpdate.assertThat().statusCode(SC_UNAUTHORIZED);
        responseUpdate.assertThat().body("message", equalTo(MESSAGE_ERROR_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Trying to replace the mail with an existing one")
    public void tryToReplaceEmailWithExisting() {
        ValidatableResponse response = userClients.createUser(currentUser);
        userClients.createUser(newUser);

        userClients.loginUser(currentUser);
        currentUser.setAllToken(response);

        ValidatableResponse responseUpdate = userClients.updateUser(currentUser, newUser);
        responseUpdate.assertThat().statusCode(SC_FORBIDDEN);
        responseUpdate.assertThat().body("message", equalTo(MESSAGE_ERROR_FORBIDDEN));

    }

}
