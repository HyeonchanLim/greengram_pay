package com.green.greengram.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.config.constants.ConstJwt;
import com.green.greengram.config.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
public class TokenProvider {
    private final ObjectMapper objectMapper; //Jackson 라이브러리
    private final ConstJwt constJwt;
    private final SecretKey secretKey;

    public TokenProvider(ObjectMapper objectMapper, ConstJwt constJwt) {
        this.objectMapper = objectMapper;
        this.constJwt = constJwt;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(constJwt.getSecret())); //43자 이상
    }

    public String generateAccessToken(JwtUser jwtUser) {
        return generateToken(jwtUser, constJwt.getAccessTokenExpiry());
    }

    public String generateRefreshToken(JwtUser jwtUser) {
        return generateToken(jwtUser, constJwt.getRefreshTokenExpiry());
    }

    public String generateToken(JwtUser jwtUser, long tokenValidMilliSecond) {
        Date now = new Date();
        return Jwts.builder()
                .header().type(constJwt.getBearerFormat())
                .and()

                .issuer(constJwt.getIssuer())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenValidMilliSecond))
                .claim(constJwt.getClaimKey(), makeClaimByUserToString(jwtUser))

                .signWith(secretKey)
                .compact();
    }

    //객체 > String : 직렬화(JSON)
    private String makeClaimByUserToString(JwtUser jwtUser) {
        //객체 자체를 JWT에 담고 싶어서 객체를 직렬화
        //jwtUser에 담고있는 데이터를 JSON형태의 문자열로 변환
        try {
            //objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
            return objectMapper.writeValueAsString(jwtUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //------ 만들어진 토큰(AT, RT)
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(constJwt.getHeaderKey());
        if(bearerToken == null || !bearerToken.startsWith(constJwt.getScheme())) {
            return null;
        }
        return bearerToken.substring(constJwt.getScheme().length() + 1); //Bearer(빈칸)까지 index를 설정해야 하기 때문

    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public JwtUser getJwtUserFromToken(String token) {
        Claims claims = getClaims(token);
        String json = claims.get(constJwt.getClaimKey(), String.class);
        try {
            return objectMapper.readValue(json, JwtUser.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Authentication getAuthentication(String token) {
        JwtUser jwtUser = getJwtUserFromToken(token);
        MyUserDetails myUserDetails = new MyUserDetails();
        myUserDetails.setJwtUser(jwtUser);
        return new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
    }

}
