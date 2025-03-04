package com.green.greengram.kakaopay;

import com.green.greengram.config.SessionUtils;
import com.green.greengram.config.constants.ConstKakaoPay;
import com.green.greengram.config.exception.CustomException;
import com.green.greengram.config.exception.PayErrorCode;
import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.entity.*;
import com.green.greengram.kakaopay.model.*;
import com.green.greengram.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayService {
    private final AuthenticationFacade authenticationFacade;
    private final ConstKakaoPay constKakaoPay;
    private final KakaoPayFeignClient kakaoPayFeignClient;
    private final OrderMasterRepository orderMasterRepository;

    private final ProductRepository productRepository;

    @Transactional
    public KakaoPayReadyRes postReady(KakaoPayReadyReq req) {
        if(req.getProductList().size() == 0) { throw new CustomException(PayErrorCode.NOT_EXISTED_PRODUCT_INFO);  }
        List<Long> productIds = req.getProductList().stream()
                                                    .mapToLong(item -> item.getProductId())
                                                    .boxed()
                                                    .toList();// 구매하려는 상품pk값만 추리고 있음
        List<Product> productList = productRepository.findByProductIdIn(productIds);
        // findByProductIdIn 쿼리문 ( select * from product where product_id in (1,2,3,4)
        if(req.getProductList().size() != productList.size()) { throw new CustomException(PayErrorCode.NO_EXISTED_PRODUCT_INFO); }

        //Product 목록을 Map으로 변환하여 빠르게 검색 가능하게 만듦 Function.identity()는 객체의 멤버필드가 아닌 객체 자신을 말한다.
        Map<Long, OrderProductDto> orderProductMap = req.getProductList().stream().collect(Collectors.toMap(OrderProductDto::getProductId, Function.identity()));



        int totalAmount = productList.stream().reduce(0 // 초기값
                , (prev, item) ->  prev + (item.getProductPrice() * orderProductMap.get(item.getProductId()).getQuantity())
                // 연산하는 데이터 값 , BiFunction Implements 한 객체 주소 값 , 첫 번째 파라미터는 이전 리턴 값 , 두 번째 파라미터 스트림 자식 순차적으로 넘어옴
                , Integer::sum); // 연산 결과 타입 설정

        // DB insert 작업 준비
        User signedUser = User.builder()
                .userId(authenticationFacade.getSignedUserId())
                .build();
        // OrderMaster 엔티티 객체화 (영속성이 없음) --- (a)라고 호칭
        // 영속성이 있으면 insert -> update 로 바뀜
        OrderMaster orderMaster = OrderMaster.builder()
                .user(signedUser)
                .totalAmount(totalAmount)
                .orderStatusCode(OrderStatusCode.READY)
                .build();

        log.info("orderMaster - orderId : {}" , orderMaster.getOrderId()); // null

        for(Product item : productList) { // DB 에서 가져온 productList 를 향상된 for문 처리
            // OrderProduct 엔티티는 복합키로 구성 (orderId + productId) , OrderProductIds 객체화 --- (b) 라고 호칭
            OrderProductIds ids = OrderProductIds.builder()
                    .productId(item.getProductId()) // orderId 값을 넣을 수 없다. 왜냐면 아직 (a)가 insert 전이기 때문에
                    .build();
            // OrderProduct 엔티티 객체화 (영속성이 없음) --- (c) 라고 호칭
            OrderProduct orderProduct = OrderProduct.builder()
                    .ids(ids)
                    .product(item)
                    .quantity(orderProductMap.get(item.getProductId()).getQuantity())
                    .unitPrice(item.getProductPrice())
                    .build();

            orderMaster.addOrderProduct(orderProduct); // 양방향 연결 (a) , (c) 연결 , (a) <-> (c) 서로 알아야 한다.
        }

        orderMasterRepository.save(orderMaster);
        orderMasterRepository.flush(); // insert 체크

        // 결제 준비 단계
        /*
        AAA 상품만 산다고 했을 때 (상품 1개)
        itemName = "AAA";

        AAA , BBB , CCC
        itemName = "AAA";
        itemName = "AAA 외 2개";
         */
        String itemName = productList.get(0).getProductName();
        if(productList.size() > 1) { // 구매하는 상품이 2개 이상일 때는 외 1개라고 출력하도록 설정
            itemName += String.format(" 외 %d개", productList.size() - 1);
        }

        KakaoPayReadyFeignReq feignReq = KakaoPayReadyFeignReq.builder()
                .cid(constKakaoPay.getCid())
                .partnerOrderId(orderMaster.getOrderId().toString()) //OrderMaster에 insert 된 orderId값
                .partnerUserId(String.valueOf(authenticationFacade.getSignedUserId())) //결제 유저 ID
                .itemName(itemName)
                .quantity(productList.size())
                .totalAmount(totalAmount)
                .taxFreeAmount(0)
                .approvalUrl(constKakaoPay.getApprovalUrl()) // 준비가 제대로 되면 redirect 주소값 
                .failUrl(constKakaoPay.getFailUrl())
                .cancelUrl(constKakaoPay.getCancelUrl())
                .build();


        KakaoPayReadyRes res = kakaoPayFeignClient.postReady(feignReq); // 결제 준비단계 요청을 보내고 응답으로 tid 를 얻을 수 있다.

        //세션에 결제 정보 저장 ( 결제 승인 때 결제 준비 단계에서 보낸 tid , partnerOrderId , partnerUserId 가 같아야 한다. 그래서 세션에 저장함
        KakaoPaySessionDto dto = KakaoPaySessionDto.builder()
                .tid(res.getTid())
                .partnerOrderId(feignReq.getPartnerOrderId())
                .partnerUserId(feignReq.getPartnerUserId())
                .build();

        SessionUtils.addAttribute(constKakaoPay.getKakaoPayInfoSessionName(), dto);
        log.info("tid: {}", res.getTid());
        return res;
    }

    public KakaoPayApproveRes getApprove(KakaoPayApproveReq req) {
        //카카오페이 준비과정에서 세션에 저장한 고유번호(tid), partnerOrderId , partnerUserId 가져오기
        KakaoPaySessionDto dto = (KakaoPaySessionDto) SessionUtils.getAttribute(constKakaoPay.getKakaoPayInfoSessionName());
        log.info("결제승인 요청을 인증하는 토큰: {}", req.getPgToken());
        //log.info("결제 고유번호: {}", tid);

        KakaoPayApproveFeignReq feignReq = KakaoPayApproveFeignReq.builder()
                .cid(constKakaoPay.getCid())
                .tid(dto.getTid())
                .partnerOrderId(dto.getPartnerOrderId())
                .partnerUserId(dto.getPartnerUserId())
                .pgToken(req.getPgToken())
                .payload("테스트")
                .build();

        KakaoPayApproveRes res = kakaoPayFeignClient.postApprove(feignReq);
        log.info("res: {}", res);

        OrderMaster orderMaster = orderMasterRepository.findById(Long.parseLong(dto.getPartnerOrderId())).orElse(null);
        if(orderMaster != null) {
            orderMaster.setOrderStatusCode(OrderStatusCode.COMPLETED);
            orderMasterRepository.save(orderMaster);
        }
        return res;
    }
}
