package com.green.greengram.feed.like;

import com.green.greengram.TestUtils;
import com.green.greengram.config.JpaAuditingConfiguration;
import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedLike;
import com.green.greengram.entity.FeedLikeIds;
import com.green.greengram.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Import(JpaAuditingConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Sql(scripts = {"classpath:test-import.sql"})
class FeedLikeRepositoryTest {
    @Autowired
    FeedLikeRepository feedLikeRepository;

    static final Long userId_1 = 1L;
    static final Long userId_2 = 2L;

    static final Long feedId_1 = 1L;
    static final Long feedId_2 = 2L;

    FeedLike existedData = FeedLike.builder()
            .feedLikeIds(FeedLikeIds.builder().userId(userId_1).feedId(feedId_1).build())
            .user(User.builder().userId(userId_1).build())
            .feed(Feed.builder().feedId(feedId_1).build())
            .build();

    FeedLike notExistedData = FeedLike.builder()
            .feedLikeIds(FeedLikeIds.builder().userId(userId_2).feedId(feedId_2).build())
            .user(User.builder().userId(userId_2).build())
            .feed(Feed.builder().feedId(feedId_2).build())

            .build();

    @BeforeEach
    void initData() {
        feedLikeRepository.deleteAll();
        feedLikeRepository.save(existedData);
    }

//    @Test
//    @DisplayName("중복된 데이터 입력시 DuplicateKeyException 발생 체크")
//    void insFeedLikeDuplicateDataThrowDuplicateKeyException() throws Exception {
//        //JPA에서는 DuplicateKeyException 발생이 되지 않는다.
//    }

    @Test
    void insFeedLike() {
        // given - void initData 메소드
        //when
        List<FeedLike> actualFeedLikeListBefore = feedLikeRepository.findAll(); //insert전 튜플 수 (expectedRows: 1)
        FeedLike actualFeedLikeBefore = feedLikeRepository.findById(FeedLikeIds.builder()
                .userId(userId_2)
                .feedId(feedId_2)
                .build())
                .orElse(null); //insert전 존재하지 않는 레코드 읽기 (expected not null)

        feedLikeRepository.save(notExistedData); // insert 후 튜플 수 : 2
        FeedLike actualFeedLikeAfter = feedLikeRepository.findById(FeedLikeIds.builder()
                .userId(userId_2)
                .feedId(feedId_2)
                .build())
                .orElse(null); //insert후 존재하는 레코드 읽기 (expected not null)
        List<FeedLike> actualFeedLikeListAfter = feedLikeRepository.findAll(); //insert후 튜플 수

        //then
        assertAll(
                () -> TestUtils.assertCurrentTimestamp(actualFeedLikeAfter.getCreatedAt())
                , () -> assertEquals(actualFeedLikeListBefore.size() + 1, actualFeedLikeListAfter.size())
                , () -> assertNull(actualFeedLikeBefore) //내가 insert하려고 하는 데이터가 없었는지 단언
                , () -> assertNotNull(actualFeedLikeAfter) //실제 내가 원하는 데이터로 insert가 되었는지 단언

                , () -> assertEquals(notExistedData.getFeed().getFeedId(), actualFeedLikeAfter.getFeed().getFeedId()) //내가 원하는 데이터로 insert 되었는지 더블 체크
                , () -> assertEquals(notExistedData.getUser().getUserId(), actualFeedLikeAfter.getUser().getUserId()) //내가 원하는 데이터로 insert 되었는지 더블 체크
        );
    }

}