package com.green.greengram.feed.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
/*
 feed_pics 테이블에 튜플 여러개를 insert
 한문장으로 처리하기 위해 사용하는 객체
 */
public class FeedPicDto {
    private long feedId;
    private List<String> pics;
}
