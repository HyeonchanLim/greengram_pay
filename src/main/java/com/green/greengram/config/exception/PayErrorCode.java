package com.green.greengram.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PayErrorCode implements ErrorCode {
    INCORRECT_TOTAL_AMOUNT(HttpStatus.BAD_REQUEST, "총 결제금액이 상이합니다.")
    , NOT_EXISTED_PRODUCT_INFO(HttpStatus.BAD_REQUEST, "결제 상품 정보가 없습니다.")
    , NO_EXISTED_PRODUCT_INFO(HttpStatus.BAD_REQUEST, "잘못된 상품 정보가 있습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
