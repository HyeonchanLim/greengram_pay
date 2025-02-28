package com.green.greengram.feed.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.beans.ConstructorProperties;

@Getter
@Schema(title = "피드 DELETE 요청")
@EqualsAndHashCode
public class FeedDeleteReq {
    @Positive
    @Schema(description = "피드 PK", name="feed_id", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;

    @ConstructorProperties({"feed_id"})
    public FeedDeleteReq(long feedId) {
        this.feedId = feedId;
    }
}
