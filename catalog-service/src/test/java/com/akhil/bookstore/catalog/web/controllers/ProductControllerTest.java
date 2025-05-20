package com.akhil.bookstore.catalog.web.controllers;

import com.akhil.bookstore.catalog.AbstractIT;
import com.akhil.bookstore.catalog.domain.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

@Sql("/test-data.sql")
class ProductControllerTest extends AbstractIT {

    @Test
    void shouldReturnProduct() {
        RestAssured.given().contentType(ContentType.JSON)
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("data", Matchers.hasSize(10))
                .body("totalElements", Matchers.is(15))
                .body("pageNumber", Matchers.is(1))
                .body("totalPages", Matchers.is(2))
                .body("isFirst", Matchers.is(true))
                .body("isLast", Matchers.is(false))
                .body("hasNext", Matchers.is(true))
                .body("hasPrevious", Matchers.is(false));
    }

    @Test
    void shouldReturnProductByCode(){
        Product product = RestAssured.given().contentType(ContentType.JSON)
                .when()
                .get("/api/products/{code}","P100")
                .then()
                .statusCode(200)
                .assertThat()
                .extract()
                .as(Product.class);

        Assertions.assertThat(product.code()).isEqualTo("P100");
        Assertions.assertThat(product.name()).isEqualTo("The Hunger Games");
        Assertions.assertThat(product.description()).isEqualTo("Winning will make you famous. Losing means certain death...");
        Assertions.assertThat(product.price()).isEqualTo(new BigDecimal("34.0"));
    }

    @Test
    void shouldReturnNotFoundWhenProductCodeNotExist(){
        String code = "invalid_product_code";
        RestAssured.given().contentType(ContentType.JSON)
                .get("/api/products/{code}", code)
                .then()
                .statusCode(404)
                .body("status", Matchers.is(404))
                .body("title", Matchers.is("Product Not Found"))
                .body("detail", Matchers.is("Product with code " + code + " not found"));
    }
}