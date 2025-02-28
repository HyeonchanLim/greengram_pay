package com.green.greengram.product;

import com.green.greengram.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductIdIn(List<Long> productIds);
}
