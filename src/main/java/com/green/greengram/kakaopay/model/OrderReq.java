package com.green.greengram.kakaopay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderReq {
    private final String itemName; //상품명
    private final int quantity;  //상품 수량
    private final int unitPrice; //상품 단가
}
