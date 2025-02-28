package com.green.greengram.feed;

import com.green.greengram.TestUtils;
import com.green.greengram.feed.model.FeedPicDto;
import com.green.greengram.feed.model.FeedPicSel;
import com.green.greengram.feed.model.FeedPicVo;
import net.bytebuddy.build.ToStringPlugin;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedPicMapperTest {
    @Autowired FeedPicMapper feedPicMapper;
    @Autowired FeedPicTestMapper feedPicTestMapper;

    @Test
    //
    void insFeedPicNoFeedIdThrowForeignKeyException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(10L);
        givenParam.setPics(new ArrayList<>(1));
        givenParam.getPics().add("a.jpg");

        assertThrows(DataIntegrityViolationException.class, () -> {
           feedPicMapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPicNullPicsThrowException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(1L);
        assertThrows(MyBatisSystemException.class, () -> {
            feedPicMapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPicNoPicThrowException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(1L);
        givenParam.setPics(new ArrayList<>());
        assertThrows(BadSqlGrammarException.class, () -> {
            feedPicMapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPic__PicStringLengthMoreThan50__ThrowException() {
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(1L);
        givenParam.setPics(new ArrayList<>(1));
        givenParam.getPics().add("_123456789_123456789_123456789_123456789_123456789_12");
        assertThrows(BadSqlGrammarException.class, () -> {
            feedPicMapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPic() {
        String[] pics = { "a.jpg", "b.jpg", "c.jpg" };
        FeedPicDto givenParam = new FeedPicDto();
        givenParam.setFeedId(5L);
        givenParam.setPics(new ArrayList<>(pics.length));
        for(String pic : pics) {
            givenParam.getPics().add(pic);
        }
        List<FeedPicVo> feedPicListBefore = feedPicTestMapper.selFeedPicListByFeedId(givenParam.getFeedId());
        int actualAffectedRows = feedPicMapper.insFeedPic(givenParam);
        List<FeedPicVo> feedPicListAfter = feedPicTestMapper.selFeedPicListByFeedId(givenParam.getFeedId());

        //feedPicListAfterd에서 pic만 뽑아내서 이전처럼 List<String>변형한 다음 체크한다.
        List<String> feedOnlyPicList = new ArrayList<>(feedPicListAfter.size());
        for(FeedPicVo feedPicVo : feedPicListAfter) {
            feedOnlyPicList.add(feedPicVo.getPic());
        }

        //스트림 이용해서 한다.
        List<String> picList = Arrays.asList(pics);
        for(int i=0; i<pics.length; i++) {
            String pic = picList.get(i);
            System.out.printf("%s - contains: %b\n", pic, feedOnlyPicList.contains(pic));
        }

        //Predicate 리턴타입 O (boolean), 파라미터 O (FeedPicVo)
        String[] pics2 = { "a.jpg", "b.jpg", "c.jpg", "d.jpg" };
        List<String> picList2 = Arrays.asList(pics2);
        feedPicListAfter.stream().allMatch(feedPicVo -> picList2.contains(feedPicVo.getPic()));

        assertAll(
              () -> feedPicListAfter.forEach(feedPicVo -> TestUtils.assertCurrentTimestamp(feedPicVo.getCreatedAt()))
            , () -> {
                  for(FeedPicVo feedPicVo : feedPicListAfter) {
                      TestUtils.assertCurrentTimestamp(feedPicVo.getCreatedAt());
                  }
              }
            , () -> assertEquals(givenParam.getPics().size(), actualAffectedRows)
            , () -> assertEquals(0, feedPicListBefore.size())
            , () -> assertEquals(givenParam.getPics().size(), feedPicListAfter.size())
            , () -> assertTrue(feedOnlyPicList.containsAll(Arrays.asList(pics)))
            , () -> assertTrue(Arrays.asList(pics).containsAll(feedOnlyPicList))
            , () -> assertTrue(feedPicListAfter.stream().allMatch(feedPicVo -> picList.contains(feedPicVo.getPic())))

            , () -> assertTrue(feedPicListAfter.stream() //스트림 생성 Stream<FeedPicVo>
                                               .map(FeedPicVo::getPic) //똑같은 크기의 새로운 반환 Stream<String> ["a.jpg", "b.jpg", "c.jpg"]
                                               .filter(pic -> picList.contains(pic)) //필터는 연산의 결과가 true인 것만 뽑아내서 새로운 스트림 반환 Stream<String> ["a.jpg", "b.jpg", "c.jpg"]
                                               .limit(picList.size()) // 스트림 크기를 제한, 이전 스트림의 크기가 10개인데 limit(2)를 하면 2개짜리 스트림이 반환된다.
                                               .count() == picList.size())
            // FeedPicVo::getPic 메소드 참조
            , () -> assertTrue(feedPicListAfter.stream().map(FeedPicVo::getPic).toList().containsAll(Arrays.asList(pics)))

                //Function return type O (String), parameter O (FeedPicVo)
            , () -> assertTrue(feedPicListAfter.stream().map(feedPicVo -> feedPicVo.getPic()) // ["a.jpg", "b.jpg", "c.jpg"]
                                    .toList() //스트림 > List
                                    .containsAll(Arrays.asList(pics)))
        );

        //created_at 단언

    }
}