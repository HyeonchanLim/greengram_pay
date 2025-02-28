package com.green.greengram.feed.like.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(title = "피드 좋아요 Toggle")
@EqualsAndHashCode
public class FeedLikeReq {
    @Positive
    @Schema(title = "피드 PK", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;
    @JsonIgnore
    @Positive
    private long userId;
}