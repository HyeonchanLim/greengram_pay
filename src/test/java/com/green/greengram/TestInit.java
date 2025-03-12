package com.green.greengram;

import org.hibernate.annotations.processing.SQL;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

/*
테스트 원상태 복귀용 or 기초 데이터 늘릴 때
entity 새로 생성 후 기초 데이터 insert
 */
@ActiveProfiles("test-init") // test-init.yml 주소 경로 설정
@Rollback(false)
// 통합 테스트 때 사용하는 어노테이션 ( 모든 빈들이 생성된다. )
//@SpringBootTest // spring 컨테이너 + bean 등록한 내용 전부 등록 ,
@Sql(scripts = {"classpath:test-import.sql"})
public class TestInit {
    @Test
    void init(){}
}
