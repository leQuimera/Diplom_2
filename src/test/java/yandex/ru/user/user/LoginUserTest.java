package yandex.ru.user.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yandex.ru.clients.UserClients;
import yandex.ru.model.User;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private User user;
    private UserClients userClients;
    private static final String MESSAGE_ERROR_UNAUTHORIZED = "email or password are incorrect";


    @Before
    public void setUp() {
        user = User.createRandomUser();
        userClients = new UserClients();

        userClients.createUser(user);
    }

    @After
    public void tearDown() {
        userClients.deleteUser(user);
    }

    @Test
    @DisplayName("User login")
    public void loginWithValidCredentials() {
        ValidatableResponse validatableResponse = userClients.loginUser(user);
        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Try to login user without email")
    public void tryToLoginUserWithoutEmail() {
        user.setEmail(null);
        ValidatableResponse validatableResponse = userClients.loginUser(user);
        validatableResponse.assertThat().statusCode(SC_UNAUTHORIZED);
        validatableResponse.assertThat().body("message", equalTo(MESSAGE_ERROR_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Try to login user with incorrect email")
    public void tryToLoginUserWithIncorrectEmail() {
        user.setEmail("myemail@not.mail");
        ValidatableResponse validatableResponse = userClients.loginUser(user);
        validatableResponse.assertThat().statusCode(SC_UNAUTHORIZED);
        validatableResponse.assertThat().body("message", equalTo(MESSAGE_ERROR_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Try to login user without password")
    public void tryToLoginUserWithoutPassword() {
        user.setPassword(null);
        ValidatableResponse validatableResponse = userClients.loginUser(user);
        validatableResponse.assertThat().statusCode(SC_UNAUTHORIZED);
        validatableResponse.assertThat().body("message", equalTo(MESSAGE_ERROR_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Try to login user with incorrect password")
    public void tryToLoginUserWithIncorrectPassword() {
        user.setPassword("setThisPassword?");
        ValidatableResponse validatableResponse = userClients.loginUser(user);
        validatableResponse.assertThat().statusCode(SC_UNAUTHORIZED);
        validatableResponse.assertThat().body("message", equalTo(MESSAGE_ERROR_UNAUTHORIZED));
    }
}
