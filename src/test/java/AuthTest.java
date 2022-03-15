import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

class AuthTest {

    @Data
    @RequiredArgsConstructor
    public static class RegistrationDto {
        private final String login;
        private final String password;
        private final String status;
    }

    @UtilityClass
    public class DataGenerator {

        private static final RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        @UtilityClass
        public class Registration {
            public static RegistrationDto generateInfo(String locale) {
                Faker faker = new Faker(new Locale("en"));
                return new RegistrationDto(
                        faker.name().firstName(),
                        faker.internet().password(),
                        "active"
                        );
            }
        }
        private static void sendRequest(RegistrationDto user) {
            given()
                    .spec(requestSpec)
                    .body(user)
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
        }
    }

    @Test
    void validUser() {
        RegistrationDto user = DataGenerator.Registration.generateInfo("en");
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
        RegistrationDto user = DataGenerator.Registration.generateInfo("en");
        DataGenerator.sendRequest(user);
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
        RegistrationDto user = DataGenerator.Registration.generateInfo("en");
        DataGenerator.sendRequest(user);
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