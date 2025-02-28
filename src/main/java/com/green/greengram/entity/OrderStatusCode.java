package com.green.greengram.entity;

import com.green.greengram.config.enumcode.AbstractEnumCodeConverter;
import com.green.greengram.config.enumcode.EnumMapperType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatusCode implements EnumMapperType {
      READY("01", "준비")
    , FAIL("02", "실패")
    , CANCEL("03", "취소")
    , COMPLETED("09", "완료")
    ;

    private final String code;
    private final String value;

    @Converter(autoApply = true)
    public static class CodeConverter extends AbstractEnumCodeConverter<OrderStatusCode> {
        public CodeConverter() {
            super(OrderStatusCode.class, false);
        }
    }
}
