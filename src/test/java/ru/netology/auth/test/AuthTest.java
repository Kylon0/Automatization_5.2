package ru.netology.auth.test;

import com.codeborne.selenide.Condition;

import org.junit.jupiter.api.Test;

import ru.netology.auth.data.*;


import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


class AuthTest {
    @Test
    void validUser() {
        DataClass.RegistrationDto user = DataClass.DataGenerator.Registration.generateInfo("en");
        DataClass.DataGenerator.sendRequest(user);
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
        DataClass.RegistrationDto user = DataClass.DataGenerator.Registration.generateInfo("en");
        DataClass.DataGenerator.sendRequest(user);
        open("http://localhost:9999");
        {
            $("[data-test-id=login] input").setValue("Alex");
            $("[data-test-id=password] input").setValue(user.password);
            $(withText("Продолжить")).click();
            $(withText("Ошибка")).shouldBe(Condition.visible);
            $("[data-test-id=error-notification]").shouldHave(Condition.text("Ошибка\n" +
                    "Ошибка! Неверно указан логин или пароль"));
        }
    }

    @Test
    void invalidPassword(){
        DataClass.RegistrationDto user = DataClass.DataGenerator.Registration.generateInfo("en");
        DataClass.DataGenerator.sendRequest(user);
        open("http://localhost:9999");
        {
            $("[data-test-id=login] input").setValue(user.login);
            $("[data-test-id=password] input").setValue("fjeiw213");
            $(withText("Продолжить")).click();
            $(withText("Ошибка")).shouldBe(Condition.visible);
            $("[data-test-id=error-notification]").shouldHave(Condition.text("Ошибка\n" +
                    "Ошибка! Неверно указан логин или пароль"));
        }
    }
}