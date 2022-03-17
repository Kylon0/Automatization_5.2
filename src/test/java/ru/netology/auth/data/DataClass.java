package ru.netology.auth.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataClass {
    @Data
    @RequiredArgsConstructor
    public static class RegistrationDto {
        public final String login;
        public final String password;
        public final String status;
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
        public static void sendRequest(RegistrationDto user) {
            given()
                    .spec(requestSpec)
                    .body(user)
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
        }
    }
}
