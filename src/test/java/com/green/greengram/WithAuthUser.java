package com.green.greengram;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) //라이프사이클 지정, 런타임동안 사용가능
@WithSecurityContext(factory = WithAuthMockUserSecurityContextFactory.class)
public @interface WithAuthUser {
    long signedUserId() default 1L;
    String[] roles() default { "ROLE_USER" };
}

