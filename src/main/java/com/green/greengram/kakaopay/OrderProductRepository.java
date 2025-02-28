package com.green.greengram.kakaopay;

import com.green.greengram.entity.OrderProduct;
import com.green.greengram.entity.OrderProductIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductIds> {
}
