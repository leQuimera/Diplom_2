package yandex.ru.model;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import lombok.Builder;
import lombok.Data;
import java.util.Locale;


@Data
@Builder
public class User {
    private String password;
    private String name;
    private String email;
    private String accessToken;
    private String refreshToken;

    @Step("Create random user with complete information")
    public static User generateRandomUser() {
        Faker faker = new Faker();
        Faker fakerRu = new Faker(new Locale("ru"));
        return User.builder()
                .password(faker.internet().password())
                .name(fakerRu.name().firstName())
                .email(faker.internet().emailAddress())
                .build();
    }


    @Step("Set user {user.name} tokens")
    public void setAllToken(ValidatableResponse response) {
        this.setAccessToken(response.extract().response().jsonPath().getString("accessToken"));
        this.setRefreshToken(response.extract().response().jsonPath().getString("refreshToken"));
    }

}
