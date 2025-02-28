package com.green.greengram.config.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "constants.jwt")
@RequiredArgsConstructor
@ToString
public class ConstJwt {
    private final String issuer;
    private final String secret;
    private final String headerKey;
    private final String claimKey;
    private final String bearerFormat;
    private final String scheme;
    private final long accessTokenExpiry;
    private final long refreshTokenExpiry;
    private final String refreshTokenCookieName;
    private final int refreshTokenCookieExpiry;
}
