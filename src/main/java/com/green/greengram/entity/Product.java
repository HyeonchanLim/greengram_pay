package com.green.greengram.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends UpdatedAt {
    @Id
    @Tsid
    private Long productId;

    @Column(nullable = false, length = 50)
    private String productName;

    @Column(nullable = false)
    private int productPrice;

    @Column(nullable = false)
    private String productDescription;
}
