package com.green.greengram.kakaopay.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
public class KakaoPayReadyReq {
    private List<OrderProductDto> productList;
}
