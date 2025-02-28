package com.green.greengram.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtils {
    public static void addAttribute(String name, Object value) {
        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).setAttribute(name, value, RequestAttributes.SCOPE_SESSION);
    }

    public static String getStringAttribute(String name) {
        return String.valueOf(getAttribute(name));
    }

    public static Object getAttribute(String name) {
        return Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }
}

/*
scope 생명주기
- page SSR 랜더링 들어올 때 사용
- request 요청이 생길 때 마다 새 스코프가 만들어 짐 (HttpServletRequest)
- session 같은 사람(브라우저) 요청이 오면 session은 하나만 만들어지고 그걸 계속 사용

(1) 크롬 실행
(1) 크롬 종료

(2) 크롬 실행

- application 전역
 */