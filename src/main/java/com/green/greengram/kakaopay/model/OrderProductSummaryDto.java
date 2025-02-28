package com.green.greengram.kakaopay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductSummaryDto {
    private Long productId;
    private int productPrice;
    private int quantity;
}
