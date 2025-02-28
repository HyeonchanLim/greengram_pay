package com.green.greengram.config.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component //빈등록
public class Constants {
    private static int default_page_size;

    @Value("${constants.default-page-size}")
    public void setDefaultPageSize(int value) {
        default_page_size = value;
    }

    public static int getDefault_page_size() {
        return default_page_size;
    }
}
