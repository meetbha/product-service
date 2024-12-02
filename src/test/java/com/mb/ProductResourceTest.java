package com.mb;

import com.product.model.Product;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.product.util.Constants.PRODUCT_EXISTS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
class ProductResourceTest {
    Random rand = new Random();
    Integer num = rand.nextInt(1000);
    private final String name = "Iphone"+num;

    @Test
    @Order(1)
    public void testListAllProducts() {
        Map<String,String> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("description", "A18 chip");
        payload.put("price", "2500.00");

        String description = "A19 chip";

        Product product = given().contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .extract()
                .as(Product.class);
        UUID id = product.getId();

        given().contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .when()
                .post("/products")
                .then()
                .statusCode(400)
                .body("details[0]", equalTo(PRODUCT_EXISTS + name));

        given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body(containsString(name));

        given()
                .pathParam("id", id.toString())
                .when()
                .get("/products/{id}")
                .then().statusCode(200)
                .body("name", equalTo(name));

        given()
                .pathParam("id", id.toString())
                .queryParam("description", description)
                .when()
                .put("/products/{id}")
                .then()
                .statusCode(200)
                .body("description", equalTo(description));

        given()
                .pathParam("id", id.toString())
                .when()
                .delete("/products/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(2)
    public void getProductByIdNotFoundTest() {
        UUID id = UUID.randomUUID();
        given()
                .pathParam("id", id)
                .when()
                .get("/products/{id}")
                .then()
                .statusCode(400)
                .body("details[0]", equalTo("Product not found with id " + id));
    }

    @Test
    @Order(3)
    public void getDeleteByIdNotFoundTest() {
        given().pathParam("id", UUID.randomUUID()).when().delete("/products/{id}").then().statusCode(404);
    }

    @Test
    @Order(4)
    public void createProductValidationTest() {
        Map<String,String> payload = new HashMap<>();
        payload.put("price", "1500.00");
        given().contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .when()
                .post("/products")
                .then()
                .statusCode(400)
                .body("details[0]", equalTo("Product name is mandatory"));
    }

    @Test
    @Order(5)
    public void methodNotAllowTest() {
        Map<String,String> payload = new HashMap<>();
        payload.put("price", "1500.00");
        given().contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .when()
                .post("/products/name")
                .then()
                .statusCode(500);
    }

}