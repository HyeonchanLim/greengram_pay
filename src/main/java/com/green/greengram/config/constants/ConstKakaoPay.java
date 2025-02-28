package com.green.greengram.config.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "constants.kakao-pay")
@RequiredArgsConstructor
public class ConstKakaoPay {
    private final String authorizationName;
    private final String secretKey;
    private final String cid;
    private final String completedUrl;
    private final String failUrl;
    private final String cancelUrl;
    private final String tidName;
    private final String kakaoPayInfoSessionName;
}
