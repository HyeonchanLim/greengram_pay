package com.green.greengram.dummy;

import com.green.greengram.entity.Product;
import com.green.greengram.product.ProductRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Locale;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductDummyGenerator {

    @Autowired private ProductRepository productRepository;
    Faker faker = new Faker(new Locale("ko"));

    @Test
    @Rollback(false)
    void generate() {
        productRepository.deleteAll();

        for(int i=0; i<100; i++) {
            int price = (int)(Math.random() * 99.0) + 1;
            Product product = Product.builder()
                    .productName(faker.boardgame().name())
                    .productPrice(price * 1000)
                    .productDescription(faker.boardgame().subdomain())
                    .build();
            productRepository.save(product);
        }
        productRepository.flush();
    }
}
