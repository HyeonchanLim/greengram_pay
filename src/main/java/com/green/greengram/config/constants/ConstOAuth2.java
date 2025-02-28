package com.green.greengram.config.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "constants.oauth2")
@RequiredArgsConstructor
public class ConstOAuth2 {
    private final String baseUri;
    private final String authorizationRequestCookieName;
    private final String redirectUriParamCookieName;
    private final int cookieExpirySeconds;
    private final List<String> authorizedRedirectUris;
}
