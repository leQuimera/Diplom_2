package yandex.ru.model;

import com.github.javafaker.Faker;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import lombok.Builder;
import lombok.Data;
import yandex.ru.clients.IngredientsClients;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomUtils.nextInt;

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

    public static Order createRandomBurger() {
        ArrayList<Object> ingredients = new ArrayList<>();
        int bunIndex = nextInt(0, 1);
        int mainIndex = nextInt(0, 8);
        int sauceIndex = nextInt(0, 3);

        IngredientsClients ingredientsClients = new IngredientsClients();
        String jsonResult = ingredientsClients.getAllListOfIngredients().extract().asString();

        List buns = JsonPath
                .using(Configuration.defaultConfiguration())
                .parse(jsonResult)
                .read("$.data[?(@.type == 'bun')]._id", List.class);

        List mains = JsonPath
                .using(Configuration.defaultConfiguration())
                .parse(jsonResult)
                .read("$.data[?(@.type == 'main')]._id", List.class);

        List sauces = JsonPath
                .using(Configuration.defaultConfiguration())
                .parse(jsonResult)
                .read("$.data[?(@.type == 'sauce')]._id", List.class);


        ingredients.add(buns.get(bunIndex));
        ingredients.add(mains.get(mainIndex));
        ingredients.add(sauces.get(sauceIndex));

        return new Order(ingredients);
    }

}
