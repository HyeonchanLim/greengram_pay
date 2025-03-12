package com.green.greengram.feed.like;

import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedLike;
import com.green.greengram.entity.FeedLikeIds;
import com.green.greengram.entity.User;
import com.green.greengram.feed.like.model.FeedLikeReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedLikeService {
    private final FeedLikeRepository feedLikeRepository;
    private final FeedLikeMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public int feedLikeToggle(Long feedId) {
        //p.setUserId(authenticationFacade.getSignedUserId());
        FeedLikeIds ids = FeedLikeIds.builder()
                .feedId(feedId)
                .userId(authenticationFacade.getSignedUserId())
                .build();
        FeedLike feedLike = feedLikeRepository.findById(ids).orElse(null);
        if(feedLike == null) {
            Feed feed = Feed.builder()
                    .feedId(feedId)
                    .build();

            User user = User.builder()
                    .userId(authenticationFacade.getSignedUserId())
                    .build();

            feedLike = FeedLike.builder()
                    .feedLikeIds(ids)
                    .feed(feed)
                    .user(user)
                    .build();

            feedLikeRepository.save(feedLike);
            return 1; //좋아요 등록이 되었을 때 return 1
        }
        feedLikeRepository.delete(feedLike);
        return 0; //좋아요 취소가 되었을 때 return 0
    }
}
