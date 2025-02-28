package com.green.greengram.kakaopay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductDto {
    private Long productId;
    private int quantity;
}
