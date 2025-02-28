package com.green.greengram;


import com.green.greengram.config.enumcode.EnumMapper;
import com.green.greengram.config.enumcode.EnumMapperValue;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("common")
@Tag(name = "공통", description = "")
public class CommonController {

    private final EnumMapper enumMapper;

    @GetMapping("code")
    public List<EnumMapperValue> getCodeList(@RequestParam("code_type") String codeType) {
        return enumMapper.get(codeType);
    }

    @GetMapping("redirect")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }

}
