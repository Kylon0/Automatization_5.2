package ru.netology.auth.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import lombok.experimental.UtilityClass;

import java.util.Locale;

import static io.restassured.RestAssured.given;

@UtilityClass
public class DataGenerator {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public class Registration {
        public static RegistrationDto generateUser(String locale, String status) {
            Faker faker = new Faker(new Locale(locale));
            return new RegistrationDto(
                    faker.name().firstName(),
                    faker.internet().password(),
                    status
            );
        }
    }

    public static void sendRequest(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Value
    public static class RegistrationDto {
        public final String login;
        public final String password;
        public final String status;
    }

}
