package com.green.greengram.kakaopay;

import com.green.greengram.entity.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterRepository extends JpaRepository<OrderMaster, Long> {
}
