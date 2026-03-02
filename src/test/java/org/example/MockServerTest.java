package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class MockServerTest {
    private static final String MOCK_URL = "https://8504a72f-29c5-46ce-a8e5-22dfbddafcf6.mock.pstmn.io";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = MOCK_URL;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void testGetSuccess() {
        given().get("/ownerName/success")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testGetUnsuccess() {
        given().get("/ownerName/unsuccess")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testPostSomething() {
        Map<String, Object> body = new HashMap<>();
        body.put("permission", "yes");

        given().queryParam("permission", "yes")
                .body(body)
                .post("/createSomething")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testPostSomethingFailed() {
        given().post("/createSomething")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testPutUpdateMe() {
        given().put("/updateMe")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDeleteWorld() {
        given().header("SessionID", "123456789")
                .delete("/deleteWorld")
                .then()
                .statusCode(410);
    }
}