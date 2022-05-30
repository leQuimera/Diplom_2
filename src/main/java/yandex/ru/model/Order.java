package yandex.ru.model;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.Locale;

public class Order {
    private ArrayList<Object> ingredients;
    private Faker faker = new Faker();
    private Faker fakerRu = new Faker(new Locale("ru"));

    public Order(ArrayList<Object> ingredients) {
        this.ingredients = ingredients;
    }

    public Order getBurgerWithoutIngredients() {
        ArrayList<Object> ingredients = new ArrayList<>();
        return new Order(ingredients);
    }

    public Order getBurgerWithIncorrectIngredients() {
        ArrayList<Object> ingredients = new ArrayList<>();
        ingredients.add(faker.internet().hashCode());
        ingredients.add(faker.internet().hashCode());
        ingredients.add(faker.internet().hashCode());
        return new Order(ingredients);
    }


}
