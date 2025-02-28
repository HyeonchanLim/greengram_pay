package com.green.greengram.config.jwt;

import com.green.greengram.config.security.MyUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCheckpointRestore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //통합 테스트때 사용
class TokenProviderTest {
    //테스트는 생성자를 이용한 DI가 불가능
    //DI방법은 필드, setter메소드, 생성자
    //테스트 때는 필드 주입방식을 사용한다.

    @Autowired //리플렉션 API을 이용해서 setter가 없어도 주입 가능
    private TokenProvider tokenProvider;

    @Test
    public void generateToken() {
        //Given (준비단계)
        JwtUser jwtUser = new JwtUser();
        jwtUser.setSignedUserId(10);

        List<String> roles = new ArrayList<>(2);
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        jwtUser.setRoles(roles);

        //When (실행단계)
        //String token = tokenProvider.generateToken(jwtUser, Duration.ofHours(3));
        String token = null;

        //Then (검증단계)
        assertNotNull(token);

        System.out.println("token: " + token);

    }


    @Test
    void getAuthentication() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJncmVlbkBncmVlbi5rciIsImlhdCI6MTczNDQwMjk4NiwiZXhwIjoxNzM0NDEzNzg2LCJzaWduZWRVc2VyIjoie1wic2lnbmVkVXNlcklkXCI6MTAsXCJyb2xlc1wiOltcIlJPTEVfVVNFUlwiLFwiUk9MRV9BRE1JTlwiXX0ifQ.CRtVju5f60KnCx9CLwIRahr72dZsSYH5G8K3EFDUFJ3GXAXULFA8ANVtI9kml8nIdW2ViPiV-kbL-Wc5EUqmkg"; //3시간 짜리

        Authentication authentication = tokenProvider.getAuthentication(token);

        assertNotNull(authentication);

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        JwtUser jwtUser = myUserDetails.getJwtUser();

        JwtUser expectedJwtUser = new JwtUser();
        expectedJwtUser.setSignedUserId(10);

        List<String> roles = new ArrayList<>(2);
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        expectedJwtUser.setRoles(roles);

        assertEquals(expectedJwtUser, jwtUser);
    }

}