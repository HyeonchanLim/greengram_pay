package com.green.greengram.feed;

import com.green.greengram.feed.model.*;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper
@Validated
public interface FeedMapper {
    int insFeed(FeedPostReq p);
    List<FeedGetRes> selFeedList(FeedGetReq p);
    List<FeedAndPicDto> selFeedWithPicList(FeedGetReq p);
    List<FeedWithPicCommentDto> selFeedWithPicAndCommentLimit4List(FeedGetReq p);

    int delFeedLikeAndFeedCommentAndFeedPic(FeedDeleteReq p);
    int delFeed(@Valid FeedDeleteReq p);
}
