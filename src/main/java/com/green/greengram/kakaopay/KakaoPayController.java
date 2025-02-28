package com.green.greengram.kakaopay;

import com.green.greengram.config.model.ResultResponse;
import com.green.greengram.kakaopay.model.KakaoPayApproveReq;
import com.green.greengram.kakaopay.model.KakaoPayReadyReq;
import com.green.greengram.kakaopay.model.KakaoPayReadyRes;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("kakao-pay")
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;

    @PostMapping("ready")
    @Operation(summary = "카카오페이 준비 처리", description = "")
    public ResultResponse<KakaoPayReadyRes> postReady(@RequestBody KakaoPayReadyReq req) {
        KakaoPayReadyRes res = kakaoPayService.postReady(req);
        return ResultResponse.<KakaoPayReadyRes>builder()
                .resultMessage("카카오페이 준비 완료")
                .resultData(res)
                .build();
    }

    @GetMapping("approve")
    @Operation(summary = "카카오페이 결제승인 처리", description = "")
    public void getApprove(HttpServletResponse response, @ParameterObject @ModelAttribute KakaoPayApproveReq req) throws IOException {
        String redirectUrl = kakaoPayService.getApprove(req);
        log.info("redirectUrl: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
