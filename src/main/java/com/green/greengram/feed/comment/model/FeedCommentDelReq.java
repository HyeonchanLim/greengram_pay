package com.green.greengram.feed.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@ToString
@EqualsAndHashCode
public class FeedCommentDelReq {
    @Schema(name = "feed_comment_id")
    private long feedCommentId;
    @JsonIgnore
    private long userId;

    @ConstructorProperties({"feed_comment_id"})
    public FeedCommentDelReq(long feedCommentId) {
        this.feedCommentId = feedCommentId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
