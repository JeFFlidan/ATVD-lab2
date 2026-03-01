package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StoreTest {
    private static final String baseUrl = "https://petstore.swagger.io/v2";
    private static final String STORE_ORDER = "/store/order";

    private final int orderId = 1212215;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
    }

    @Test
    public void verifyPostOrder() {
        Map<String, ?> body = Map.of(
                "id", orderId,
                "petId", 1,
                "quantity", 1,
                "shipDate", "2026-03-01T12:00:00.000Z",
                "status", "placed",
                "complete", true
        );

        given().body(body)
                .post(STORE_ORDER)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(orderId));
    }

    @Test(dependsOnMethods = "verifyPostOrder")
    public void verifyGetOrder() {
        given().pathParam("orderId", orderId)
                .get(STORE_ORDER + "/{orderId}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(orderId));
    }

    @Test(dependsOnMethods = "verifyGetOrder")
    public void verifyDeleteOrder() {
        given().pathParam("orderId", orderId)
                .delete(STORE_ORDER + "/{orderId}")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}