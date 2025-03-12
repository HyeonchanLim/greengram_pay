package com.green.greengram.config.model;

import com.green.greengram.config.enumcode.AbstractEnumCodeConverter;
import com.green.greengram.config.enumcode.EnumMapperType;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumUserRole implements EnumMapperType {
    ADMIN("01", "관리자")
    , MENTOR("02", "멘토")
    , USER("03", "유저")
    ;

    private final String code;
    private final String value;

    @Converter(autoApply = true)
    public static class CodeConverter extends AbstractEnumCodeConverter<EnumUserRole> {
        public CodeConverter() {
            super(EnumUserRole.class, false);
        }
    }
}
