package com.green.greengram.product;

import com.green.greengram.config.model.ResultResponse;
import com.green.greengram.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("product")
@Tag(name = "상품", description = "상품 관리")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "상품 리스트", description = "")
    public ResultResponse<Page<Product>> getProductList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "100") int size) {
        Page<Product> res = productService.getProductList(PageRequest.of(page - 1, size)); //Pageable은 페이지 시작값이 0이기 때문에 -1처리를 한다.

        return ResultResponse.<Page<Product>>builder()
                .resultMessage("상품 리스트")
                .resultData(res)
                .build();
    }
}
