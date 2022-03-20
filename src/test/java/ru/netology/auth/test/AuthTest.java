package ru.netology.auth.test;

import com.codeborne.selenide.Condition;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import ru.netology.auth.data.*;


import java.util.Locale;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


class AuthTest {
    @Test
    void validUser() {
        DataGenerator.RegistrationDto user = DataGenerator.Registration.generateUser("en", "active");
        DataGenerator.sendRequest(user);
        open("http://localhost:9999");
        {
            $("[data-test-id=login] input").setValue(user.login);
            $("[data-test-id=password] input").setValue(user.password);
            $(withText("Продолжить")).click();
            $("[id=root]").shouldHave(Condition.text("Личный кабинет"));
        }
    }

    @Test
        void invalidLogin(){
        DataGenerator.RegistrationDto user = DataGenerator.Registration.generateUser("en", "active");
        DataGenerator.sendRequest(user);
        open("http://localhost:9999");
        Faker faker = new Faker(new Locale("en"));
        String randomName = faker.name().firstName();
        open("http://localhost:9999");
        {
            $("[data-test-id=login] input").setValue(randomName);
            $("[data-test-id=password] input").setValue(user.password);
            $(withText("Продолжить")).click();
            $(withText("Ошибка")).shouldBe(Condition.visible);
            $("[data-test-id=error-notification]").shouldHave(Condition.text("Ошибка\n" +
                    "Ошибка! Неверно указан логин или пароль"));
        }
    }

    @Test
    void invalidPassword(){
        DataGenerator.RegistrationDto user = DataGenerator.Registration.generateUser("en", "active");
        DataGenerator.sendRequest(user);
        open("http://localhost:9999");
        Faker faker = new Faker(new Locale("en"));
        String randomPassword = faker.internet().password();
        {
            $("[data-test-id=login] input").setValue(user.login);
            $("[data-test-id=password] input").setValue(randomPassword);
            $(withText("Продолжить")).click();
            $(withText("Ошибка")).shouldBe(Condition.visible);
            $("[data-test-id=error-notification]").shouldHave(Condition.text("Ошибка\n" +
                    "Ошибка! Неверно указан логин или пароль"));
        }
    }

    @Test
    void userUnregistered(){
        DataGenerator.RegistrationDto user = DataGenerator.Registration.generateUser("en", "blocked");
        DataGenerator.sendRequest(user);
        open("http://localhost:9999");
        {
            $("[data-test-id=login] input").setValue(user.login);
            $("[data-test-id=password] input").setValue(user.password);
            $(withText("Продолжить")).click();
            $(withText("Ошибка")).shouldBe(Condition.visible);
            $("[data-test-id=error-notification]").shouldHave(Condition.text("Ошибка\n" +
                    "Ошибка! Пользователь заблокирован"));
        }
    }
}