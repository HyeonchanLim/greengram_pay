package com.green.greengram;

import com.green.greengram.config.jwt.JwtUser;
import com.green.greengram.config.security.MyUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithAuthMockUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) { //annotation으로 WithAuthUser정보가 주입된다.
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        List<String> roles = List.of(annotation.roles()); // 배열 > List 컨버팅

        JwtUser jwtUser = new JwtUser();
        jwtUser.setSignedUserId(annotation.signedUserId());
        jwtUser.setRoles(roles);

        MyUserDetails myUserDetails = new MyUserDetails();
        myUserDetails.setJwtUser(jwtUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
        securityContext.setAuthentication(auth); //이 작업이 필요했음, 시큐리티가 인증이 되었다고 처리가 된다.

        return securityContext; //테스트 때 사용하는 인증처리가 될 것이다.
    }
}
