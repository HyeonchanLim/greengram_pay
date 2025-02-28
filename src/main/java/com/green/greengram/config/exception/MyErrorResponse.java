package com.green.greengram.config.exception;

import com.green.greengram.config.model.ResultResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@SuperBuilder
public class MyErrorResponse extends ResultResponse<String> {
    //Validation 에러메시지 전달
    private final List<ValidationError> valids;

    //Validation 에러가 발생시, 해당 에러의 메세지
    //어떤 필드였고, 에러 메세지를 묶음으로 담을 객체를 만들때 사용
    @Getter
    @Builder
    public static class ValidationError {
        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
