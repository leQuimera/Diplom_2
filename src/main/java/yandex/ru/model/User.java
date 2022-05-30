package yandex.ru.model;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
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

    @Step("Create random user with complete information")
    public static User createRandomUser() {
        Faker faker = new Faker();
        Faker fakerRu = new Faker(new Locale("ru"));
        return User.builder()
                .password(faker.internet().password())
                .name(fakerRu.name().firstName())
                .email(faker.internet().emailAddress())
                .build();
    }

}
