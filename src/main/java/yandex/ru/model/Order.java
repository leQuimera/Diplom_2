package yandex.ru.model;

import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import lombok.Builder;
import lombok.Data;
import yandex.ru.clients.IngredientsClients;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
public class Order {
    private List<Object> ingredients;

    public static Order getBurgerWithoutIngredients() {
        List<Object> ingredients = new ArrayList<>();
        return new Order(ingredients);
    }

    public static Order getBurgerWithIncorrectIngredients() {
        Faker faker = new Faker();
        List<Object> ingredients = new ArrayList<>();
        ingredients.add(faker.internet().hashCode());
        ingredients.add(faker.internet().hashCode());
        return new Order(ingredients);
    }

    public static Order generateRandomBurger() {
        ArrayList<Object> ingredients = new ArrayList<>();
        IngredientsClients ingredientsClients = new IngredientsClients();
        ValidatableResponse jsonResult = ingredientsClients.getAllListOfIngredients();

        ArrayList<String> buns = jsonResult.extract().body().path("data.findAll { it.type == \"bun\"}._id");
        ArrayList<String> mains = jsonResult.extract().body().path("data.findAll { it.type == \"main\"}._id");
        ArrayList<String> sauces = jsonResult.extract().body().path("data.findAll { it.type == \"sauce\"}._id");

        int bunIndex = (int) (Math.random() * buns.size());
        int mainIndex = (int) (Math.random() * mains.size());
        int sauceIndex = (int) (Math.random() * sauces.size());

        ingredients.add(buns.get(bunIndex));
        ingredients.add(mains.get(mainIndex));
        ingredients.add(sauces.get(sauceIndex));

        return new Order(ingredients);
    }

}
