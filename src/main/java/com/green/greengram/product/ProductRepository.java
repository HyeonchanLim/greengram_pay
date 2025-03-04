package com.green.greengram.product;

import com.green.greengram.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //SELECT * FROM product WHERE product_id in (1, 2, 3);
    List<Product> findByProductIdIn(List<Long> productIds);
}
