package com.green.greengram.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(title = "유저 정보 GET 응답")
public class UserInfoGetRes {
    private long userId;
    private String pic;
    private LocalDateTime createdAt;
    private String nickName;
    private int follower;
    private int following;
    @Schema(title = "등록한 피드 수")
    private int feedCount;
    @Schema(title = "피드 좋아요 숫자", description = "프로필 사용자의 피드에 달린 좋아요 수")
    private int myFeedLikeCount;
    private int followState;
}
