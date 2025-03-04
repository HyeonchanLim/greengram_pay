package com.green.greengram.kakaopay;

import com.green.greengram.config.feignclient.FeignClientKakaoPayConfiguration;
import com.green.greengram.kakaopay.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kakaoPayApi"
        , url = "${constants.kakao-pay.base-url}"
        , configuration = { FeignClientKakaoPayConfiguration.class })
public interface KakaoPayFeignClient {

    @PostMapping(value = "/ready")
    KakaoPayReadyRes postReady(@RequestBody KakaoPayReadyFeignReq req);

    @PostMapping(value = "/approve")
    KakaoPayApproveRes postApprove(KakaoPayApproveFeignReq req);
}
