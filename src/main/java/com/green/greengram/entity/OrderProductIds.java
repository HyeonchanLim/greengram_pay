package com.green.greengram.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductIds implements Serializable {
    private Long orderId;
    private Long productId;
}
