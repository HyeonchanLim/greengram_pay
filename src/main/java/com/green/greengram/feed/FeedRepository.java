package com.green.greengram.feed;

import com.green.greengram.entity.Feed;
import com.green.greengram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    //쿼리 메소드
    Optional<Feed> findByFeedIdAndWriterUser(Long feedId, User writerUser);

    //쿼리 메소드로 delete, update는 비추천
    int deleteByFeedIdAndWriterUser(Long feedId, User writerUser);

    //JPQL (Java Persistence Query Language)
    @Modifying //이 애노테이션이 있어야 delete or update JPQL, 리턴타입은 void or int
    @Query("delete from Feed f where f.feedId=:feedId AND f.writerUser.userId=:writerUserId")
    int deleteFeed(Long feedId, Long writerUserId);


    @Modifying
    @Query(value="delete from feed f where f.feed_id=:feedId and f.writer_user_id=:writerUserId"
            , nativeQuery = true)
    int deleteFeedSql(Long feedId, Long writerUserId);
    /*
    Feed (대문자로 시작) - 클래스명 작성해야 함

    feedId=1, writerUserId=2 가정하에 아래 SQL문이 만들어진다.

    DELETE f FROM feed f
    WHERE f.feed_id = 1
    AND f.user_id = 2

     */
}
