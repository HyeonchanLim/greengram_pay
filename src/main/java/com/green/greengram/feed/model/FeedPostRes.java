package com.green.greengram.feed.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@Schema(title = "피드 등록 응답")
@ToString
@EqualsAndHashCode
public class FeedPostRes {
    @Schema(title="피드 PK")
    private long feedId;
    @Schema(title="피드 사진 리스트")
    private List<String> pics;
}
