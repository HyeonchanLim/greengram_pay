package com.green.greengram.feed.comment;

import com.green.greengram.config.model.ResultResponse;
import com.green.greengram.feed.comment.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("feed/comment")
@Tag(name = "4. 피드 댓글", description = "피드 댓글 관리")
public class FeedCommentController {
    private final FeedCommentService service;

    @PostMapping
    @Operation(summary = "피드 댓글 등록", description = "")
    public ResultResponse<Long> postFeedComment(@RequestBody FeedCommentPostReq p) {
        log.info("FeedCommentController > postFeedComment > p: {}", p);
        long feedCommentId = service.postFeedComment(p);
        return ResultResponse.<Long>builder()
                .resultMessage("댓글 등록 완료")
                .resultData(feedCommentId)
                .build();
    }

    @GetMapping
    @Operation(summary = "피드 댓글 리스트", description = "댓글 더보기 처리 - 파라미터를 ModelAttribute를 이용해서 받음")
    public ResultResponse<FeedCommentGetRes> getFeedComment(@Valid @ParameterObject @ModelAttribute FeedCommentGetReq p) {
        log.info("FeedCommentController > getFeedComment > p: {}", p);
        FeedCommentGetRes res = service.getFeedComment(p);
        return ResultResponse.<FeedCommentGetRes>builder()
                .resultMessage(String.format("%d rows", res.getCommentList().size()))
                .resultData(res)
                .build();
    }

    @GetMapping("/request_param")
    @Operation(summary = "피드 댓글 리스트", description = "댓글 더보기 처리 - 파라미터를 RequestParam을 이용해서 받음")
    public ResultResponse<FeedCommentGetRes> getFeedComment2(@Parameter(description = "피드 PK", example = "12") @RequestParam("feed_id") long feedId
                                                           , @Parameter(description = "튜플 시작 index", example = "3") @RequestParam("start_idx") int startIdx
                                                             , @Parameter(description = "페이지 당 아이템 수", example = "20") @RequestParam(required = false, defaultValue = "20") int size) {
        FeedCommentGetReq p = new FeedCommentGetReq(feedId, startIdx, size);
        log.info("FeedCommentController > getFeedComment > p: {}", p);
        FeedCommentGetRes res = service.getFeedComment(p);
        return ResultResponse.<FeedCommentGetRes>builder()
                .resultMessage(String.format("%d rows", res.getCommentList().size()))
                .resultData(res)
                .build();
    }

    //삭제시 받아야 할 데이터 feedCommentId + 로그인한 사용자의 PK  (feed_comment_id, signed_user_id)
    //FE - data 전달방식 : Query-String
    @DeleteMapping
    public ResultResponse<Integer> delFeedComment(@ParameterObject @ModelAttribute FeedCommentDelReq p) {
        log.info("FeedCommentController > delFeedComment > p: {}", p);
        service.delFeedComment(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("댓글 삭제가 완료되었습니다.")
                .resultData(1)
                .build();
    }

}
