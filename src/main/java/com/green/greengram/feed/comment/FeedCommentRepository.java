package com.green.greengram.feed.comment;

import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
//    @Query(" SELECT A.feedCommentId, A.comment " +
//            " , B.userId AS writerUserId, B.nickName AS writerNm " +
//            " , B.pic AS writerPic " +
//            " FROM FeedComment A inner join User B" +
//            " ON A.user.userId = B.userId " +
//            " WHERE A.feed.feedId = :#{#req.feedId} ")
//    List<FeedCommentDto> findAllByPage(@Param("req") FeedCommentGetReq req);

//    @Query(value = "select fc from FeedComment fc join fetch fc.user WHERE fc.feed.feedId = :feedId limit :startIdx, :size", nativeQuery = true)
//    List<FeedComment> findAllByFeedIdPage(long feedId, int startIdx, int size);
}
