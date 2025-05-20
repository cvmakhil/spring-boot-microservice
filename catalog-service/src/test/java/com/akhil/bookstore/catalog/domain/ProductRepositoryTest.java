package com.akhil.bookstore.catalog.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
        properties = {
                "spring.test.database.replace=none",
                "spring.datasource.url=jdbc:tc:postgresql:16-alpine://db"
        })
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        Assertions.assertThat(products).hasSize(15);
    }

    void shouldGetProductByCode() {
        ProductEntity productEntity = productRepository.findByCode("P100").orElseThrow();
        Assertions.assertThat(productEntity.getCode()).isEqualTo("P100");
        Assertions.assertThat(productEntity.getName()).isEqualTo("The Hunger Games");
        Assertions.assertThat(productEntity.getDescription()).isEqualTo("Winning will make you famous. Losing means certain death...");
        Assertions.assertThat(productEntity.getPrice()).isEqualTo("34.0");
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotExist() {
        Assertions.assertThat(productRepository.findByCode("invalid_product_code")).isEmpty();
    }
}