package com.green.greengram.feed;

import com.green.greengram.config.exception.CustomException;
import com.green.greengram.feed.model.FeedPicDto;
import com.green.greengram.feed.model.FeedPostReq;
import com.green.greengram.feed.model.FeedPostRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class FeedServiceForPostFeedTest extends FeedServiceParentTest {

    /*
    given - 준비단계
    when - 실행
    then - 단언(검증)
     */

    @Test
    @DisplayName("Insert시 영향받은 행이 0일 때, 예외 발생")
    void postFeedInsRows0ThrowException() {
        given(authenticationFacade.getSignedUserId())
                .willReturn(SIGNED_USER_ID);

        FeedPostReq givenParam = new FeedPostReq();
        givenParam.setWriterUserId(SIGNED_USER_ID);
        givenParam.setLocation(LOCATION);
        given(feedMapper.insFeed(givenParam)).willReturn(0);

        FeedPostReq actualParam = new FeedPostReq();
        actualParam.setLocation(LOCATION);

        assertThrows(CustomException.class
                  , () -> feedService.postFeed(null, actualParam)
        );
    }

    @Test
    @DisplayName("MyFileUtils의 transferTo 호출시 잘못된 경로면, 예외 발생")
    void test2() throws Exception {
        given(authenticationFacade.getSignedUserId()).willReturn(SIGNED_USER_ID);

        FeedPostReq givenParam = new FeedPostReq();
        givenParam.setWriterUserId(SIGNED_USER_ID);
        givenParam.setLocation(LOCATION);
        given(feedMapper.insFeed(givenParam)).will(invocation -> {
            FeedPostReq invocationParam = (FeedPostReq) invocation.getArgument(0); //아래에 actualParam 주소값이 넘어온다.
            invocationParam.setFeedId(FEED_ID_10); //actualParam에 feedId = 10이 담기게 된다.
            return 1; //insFeed메소드의 리턴값
        });
        final String SAVED_PIC_NAME_1 = "abc.jpg";
        MultipartFile mpf1 = new MockMultipartFile("pics", "test1.txt", "text/plain", "This is test1 file".getBytes());
        given(myFileUtils.makeRandomFileName(mpf1)).willReturn(SAVED_PIC_NAME_1);

        final String UPLOAD_PATH = "/home/download";
        given(myFileUtils.getUploadPath()).willReturn(UPLOAD_PATH);

        String expectedMiddlePath = String.format("feed/%d", FEED_ID_10);
        String givenFilePath1 = String.format("%s/%s", expectedMiddlePath, SAVED_PIC_NAME_1);

        //return 메소드인 경우 given(임무부여) 방법
        given(myFileUtils.makeRandomFileName(mpf1)).willReturn(SAVED_PIC_NAME_1);

        //void 메소드인 경우 given(임무부여) 방법, 주의사항: when()안에서 메소드 호출을 작성하면 안된다.
        doAnswer(invoctaion -> {
            throw new IOException();
        }).when(myFileUtils).transferTo(mpf1, givenFilePath1);

        assertAll(
            () -> {
                List<MultipartFile> pics = new ArrayList<>(1);
                pics.add(mpf1);
                FeedPostReq actualParam = new FeedPostReq();
                actualParam.setLocation(LOCATION);
                assertThrows(CustomException.class, () -> feedService.postFeed(pics, actualParam));
            }
                //myFileUtils.makeFolders메소드가 호출이 되었고, 정확한 파라미터값("/feed/10")이 들어왔는지 단언
            , () -> verify(myFileUtils).makeFolders(expectedMiddlePath)
            , () -> {
                String expectedDelFolderPath = String.format("%s/%s", UPLOAD_PATH, expectedMiddlePath);
                verify(myFileUtils).deleteFolder(expectedDelFolderPath, true);
            }
        );
    }

    @Test
    @DisplayName("test2과 같은 맥락. 파일을 2개로 테스트 함. test2_1보다는 test2가 낫다.")
    void test2_1() throws Exception {
        given(authenticationFacade.getSignedUserId()).willReturn(SIGNED_USER_ID);

        FeedPostReq givenParam = new FeedPostReq();
        givenParam.setWriterUserId(SIGNED_USER_ID);
        givenParam.setLocation(LOCATION);
        given(feedMapper.insFeed(givenParam)).will(invocation -> {
            FeedPostReq invocationParam = (FeedPostReq) invocation.getArgument(0);
            invocationParam.setFeedId(FEED_ID_10);
            return 1;
        });
        final String SAVED_PIC_NAME_1 = "abc.jpg";
        final String SAVED_PIC_NAME_2 = "def.jpg";
        MultipartFile mpf1 = new MockMultipartFile("pics", "test1.txt", "text/plain", "This is test1 file".getBytes());
        MultipartFile mpf2 = new MockMultipartFile("pics", "test2.txt", "text/plain", "This is test2 file".getBytes());
        given(myFileUtils.makeRandomFileName(mpf1)).willReturn(SAVED_PIC_NAME_1);
        given(myFileUtils.makeRandomFileName(mpf2)).willReturn(SAVED_PIC_NAME_2);

        String expectedMiddlePath = String.format("feed/%d", FEED_ID_10);
        String givenFilePath1 = String.format("%s/%s", expectedMiddlePath, SAVED_PIC_NAME_1);
        String givenFilePath2 = String.format("%s/%s", expectedMiddlePath, SAVED_PIC_NAME_2);

        doAnswer(invoctaion -> {
            return null;
        }).when(myFileUtils).transferTo(mpf1, givenFilePath1);

        doAnswer(invoctaion -> {
            throw new IOException();
        }).when(myFileUtils).transferTo(mpf2, givenFilePath2);

        List<MultipartFile> pics = new ArrayList<>(2);
        pics.add(mpf1);
        pics.add(mpf2);

        FeedPostReq actualParam = new FeedPostReq();
        actualParam.setLocation(LOCATION);
        assertThrows(CustomException.class, () -> feedService.postFeed(pics, actualParam));
        verify(myFileUtils).makeFolders(expectedMiddlePath); //verify는 메소드가 원하는 파라미터로 호출되었는지 확인할 수 있다.
    }


    @Test
    @DisplayName("정상 처리")
    void test3() throws Exception {
        final long FEED_ID = FEED_ID_10;
        given(authenticationFacade.getSignedUserId()).willReturn(SIGNED_USER_ID);

        FeedPostReq givenParam = new FeedPostReq();
        givenParam.setWriterUserId(SIGNED_USER_ID);
        givenParam.setLocation(LOCATION);
        given(feedMapper.insFeed(givenParam)).will(invocation -> {
            FeedPostReq invocationParam = (FeedPostReq) invocation.getArgument(0);
            invocationParam.setFeedId(FEED_ID);
            return 1;
        });
        final List<String> SAVED_FILE_NAMES = Arrays.asList("abc.jpg", "def.jpg");
        final List<MultipartFile> PICS = Arrays.asList(
                  new MockMultipartFile("pics", "test1.txt", "text/plain", "This is test1 file".getBytes())
                , new MockMultipartFile("pics", "test2.txt", "text/plain", "This is test2 file".getBytes())
        );
        for(int i=0; i<SAVED_FILE_NAMES.size(); i++) {
            String picName = SAVED_FILE_NAMES.get(i);
            MultipartFile mpf = PICS.get(i);
            given(myFileUtils.makeRandomFileName(mpf)).willReturn(picName);
        }

        FeedPicDto expectedFeedPicDto = new FeedPicDto();
        expectedFeedPicDto.setFeedId(FEED_ID);
        expectedFeedPicDto.setPics(SAVED_FILE_NAMES);

        FeedPostRes expectedResult = FeedPostRes.builder()
                                                .feedId(FEED_ID)
                                                .pics(SAVED_FILE_NAMES)
                                                .build();

        FeedPostReq actualParam = new FeedPostReq();
        actualParam.setLocation(LOCATION);

        FeedPostRes actualResult = feedService.postFeed(PICS, actualParam);

        String expectedMiddlePath = String.format("feed/%d", FEED_ID_10);
        assertAll(
                  () -> assertEquals(expectedResult, actualResult)
                , () -> verify(feedPicMapper).insFeedPic(expectedFeedPicDto)
                , () -> {
                      for(int i=0; i<SAVED_FILE_NAMES.size(); i++) {
                          String picName = SAVED_FILE_NAMES.get(i);
                          MultipartFile mf = PICS.get(i);
                          String filePath = String.format("%s/%s", expectedMiddlePath, picName);
                          verify(myFileUtils).transferTo(mf, filePath);
                      }
                }
        );
    }
}