package com.green.greengram.config.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    //나를 상속받은 ENUM은 String message 멤버필드를 꼭 가져야 한다.
    String name();
    String getMessage();
    HttpStatus getHttpStatus(); //응답 코드 결정
}
