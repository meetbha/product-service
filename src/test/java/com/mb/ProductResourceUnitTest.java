package com.mb;

import com.product.exception.ProductNotFoundException;
import com.product.model.Product;
import com.product.service.ProductService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;
import static org.jboss.resteasy.reactive.RestResponse.Status.NOT_FOUND;
import static org.jboss.resteasy.reactive.RestResponse.Status.NO_CONTENT;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class ProductResourceUnitTest {

    @InjectMock
    ProductService productServiceMock;

    @Test
    public void addProductTest() {
        String payload = "{\"name\":\"Iphone 16\",\"description\":\"A18 chip\",\"price\":\"2500.00\"}";
        Mockito.when(productServiceMock.createProduct(any())).thenReturn(Uni.createFrom().item(Response.status(201).entity(getProducts().get(2)).build()));
        Product product = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .when()
                .post("/api/products")
                .then()
                .statusCode(201).extract().as(Product.class);
        Assertions.assertEquals(product.getName(), "Iphone 16");
    }

    @Test
    public void getProductsTest() {
        Mockito.when(productServiceMock.getAllProducts()).thenReturn(Uni.createFrom().item(Response.status(OK).entity(getProducts()).build()));
        Product[] product = RestAssured.when().get("/api/products").then().statusCode(200).extract().as(Product[].class);
        Assertions.assertEquals(product[0].getName(), "Airpods pro");
    }


    @Test
    public void getProductByIdTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(productServiceMock.getProductById(id)).thenReturn(Uni.createFrom().item(getProducts().get(1)));
        Product product = RestAssured.given().pathParam("id", id.toString()).when().get("/api/products/{id}").then().statusCode(200).extract().as(Product.class);
        Assertions.assertEquals(product.getName(), "Iphone 15");
    }

    @Test
    public void getProductByIdNotFoundTest() {
        UUID id = UUID.randomUUID();
        String errorResponse = "{\"statusCode\":400,\"messages\":\"Validation failed\",\"details\":[\"Product not found\"]}";
        Mockito.when(productServiceMock.getProductById(id)).thenReturn(Uni.createFrom().failure(new ProductNotFoundException("Product not found")));
        RestAssured.given().pathParam("id", id.toString()).when().get("/api/products/{id}").then().statusCode(400)
                .body("details[0]", equalTo("Product not found"));
    }

    @Test
    public void getDeleteByIdTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(productServiceMock.deleteProductById(id)).thenReturn(Uni.createFrom().item(Response.ok().status(NO_CONTENT).build()));
        RestAssured.given().pathParam("id", id.toString()).when().delete("/api/products/{id}").then().statusCode(204);
    }

    @Test
    public void getDeleteByIdNotFoundTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(productServiceMock.deleteProductById(id)).thenReturn(Uni.createFrom().item(Response.ok().status(NOT_FOUND).build()));
        RestAssured.given().pathParam("id", id.toString()).when().delete("/api/products/{id}").then().statusCode(404);
    }

    @Test
    public void updateByIdTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(productServiceMock.updateProduct(id, "2rd generation")).thenReturn(Uni.createFrom()
                .item(Response.status(OK).entity(getProducts().get(0)).build()));
        Product product = RestAssured.given().pathParam("id", id).queryParam("description", "2rd generation")
                .when().put("/api/products/{id}").then().statusCode(200).extract().as(Product.class);
        Assertions.assertNotNull(product);
        Assertions.assertEquals(product.getName(), "Airpods pro");
    }

    private List<Product> getProducts() {
        Product iphone16 = new Product(UUID.randomUUID(), "Iphone 16", "A18 chip", new BigDecimal("77000.00"), new Date());
        Product iphone15 = new Product(UUID.randomUUID(), "Iphone 15", "A17 chip", new BigDecimal("60000.00"), new Date());
        Product airpods = new Product(UUID.randomUUID(), "Airpods pro", "3rd generation", new BigDecimal("15000.00"), new Date());
        List<Product> products = new ArrayList<>(List.of(iphone15, iphone16, airpods));
        products.sort(Comparator.comparing(Product::getName));
        return products;
    }
}
