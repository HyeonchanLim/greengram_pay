package com.green.greengram.user.follow;

import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.user.follow.model.UserFollowReq;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

//Spring Test Context (컨테이너) 이용하는거 아니다.
@ExtendWith(MockitoExtension.class)
class UserFollowServiceTest {
    @InjectMocks
    UserFollowService userFollowService; //Mockito가 객체화를 직접 한다.

    @Mock
    UserFollowMapper userFollowMapper;

    @Mock
    AuthenticationFacade authenticationFacade;

    static final long fromUserId1 = 1L;
    static final long toUserId2 = 2L;
    static final long fromUserId3 = 3L;
    static final long toUserId4 = 4L;

    @Test
    @DisplayName("postUserFollow 테스트")
    void postUserFollow() {
       //given
       //authenticationFacade Mock객체의 getSignedUserId()메소드를 호출하면 willReturn값이 리턴이 되게끔 세팅
       final int EXPECTED_RESULT = 13;
       final long EXPECTED_FROM_USER_ID = fromUserId3;
       final long EXPECTED_TO_USER_ID = toUserId4;
       given(authenticationFacade.getSignedUserId()).willReturn(EXPECTED_FROM_USER_ID);

       UserFollowReq givenParam = new UserFollowReq(EXPECTED_TO_USER_ID);
       givenParam.setFromUserId(EXPECTED_FROM_USER_ID);
       given(userFollowMapper.insUserFollow(givenParam)).willReturn(EXPECTED_RESULT);

       //when
       UserFollowReq actualParam = new UserFollowReq(EXPECTED_TO_USER_ID);
       int actualResult = userFollowService.postUserFollow(actualParam);

       //then
       assertEquals(EXPECTED_RESULT, actualResult);
   }

    @Test
    @DisplayName("deleteUserFollow 테스트")
    void deleteUserFollow() {
        //given
        //authenticationFacade Mock객체의 getSignedUserId()메소드를 호출하면 willReturn값이 리턴이 되게끔 세팅
        final int EXPECTED_RESULT = 14;
        final long FROM_USER_ID = fromUserId3;
        final long TO_USER_ID = toUserId4;
        given(authenticationFacade.getSignedUserId()).willReturn(FROM_USER_ID);

        UserFollowReq givenParam = new UserFollowReq(TO_USER_ID);
        givenParam.setFromUserId(FROM_USER_ID);
        given(userFollowMapper.delUserFollow(givenParam)).willReturn(EXPECTED_RESULT);

        //when
        UserFollowReq actualParam = new UserFollowReq(TO_USER_ID);
        int actualResult = userFollowService.deleteUserFollow(actualParam);

        //then
        assertEquals(EXPECTED_RESULT, actualResult);
    }
}