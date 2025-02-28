package com.green.greengram.config;

import com.green.greengram.config.enumcode.EnumMapper;
import com.green.greengram.entity.OrderStatusCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnumCodeConfiguration {

    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put(OrderStatusCode.class.getSimpleName(), OrderStatusCode.class);
        return enumMapper;
    }

}
