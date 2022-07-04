package com.example.triple.service.point;

import com.example.triple.dto.events.EventsResponse;
import com.example.triple.dto.review.ReviewAction;
import com.example.triple.dto.review.ReviewResponse;
import com.example.triple.entity.review.Review;
import com.example.triple.entity.user.User;
import com.example.triple.error.UserCustomException;
import com.example.triple.repository.review.ReviewRepository;
import com.example.triple.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PointHistoryServiceTest {

    @Autowired
    PointHistoryService pointHistoryService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;


    public User user() {
        User user = userRepository.save(new User("triple"));
        return user;
    }

    public Review review() {
        User user = user();
        UUID placeId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse review = new EventsResponse("REVIEW", ReviewAction.ADD,
                reviewId, "테스트", attachedPhotoIds, user.getId(), placeId);
        ReviewResponse events = pointHistoryService.events(review);
        return reviewRepository.findReview(events.getReviewId(), user);
    }

    public void reviewWrite(User user, UUID reviewId, UUID placeId, String content) {

        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse firstReview = new EventsResponse("REVIEW", ReviewAction.ADD,
                reviewId, content, attachedPhotoIds, user.getId(), placeId);
        pointHistoryService.events(firstReview);
    }

    @Test
    @DisplayName("이벤트 메시지의 Action이 ADD일 때 글자수 1글자 이상 포인트 1점을 획득합니다.")
    void reviewActionADD_Content_Notnull_PlusPoint() {

        //given
        User user = user();
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();

        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD,
                reviewId, "테스트", attachedPhotoIds, user.getId(), placeId);
        //when
        ReviewResponse events = pointHistoryService.events(eventsResponse);
        Review review = reviewRepository.findReview(events.getReviewId(), user);

        //then
        assertThat(review.getContentPoint()).isEqualTo(1);
    }

    @Test
    @DisplayName("이벤트 메시지의 Action이 ADD일 때 특정 장소에서 첫번째로 리뷰를 작성 시 포인트 1점을 획득합니다.")
    void reviewActionADD_First_SpecialReview() {

        //given
        User user = user();
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();


        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD,
                reviewId, "테스트", attachedPhotoIds, user.getId(), placeId);
        //when
        ReviewResponse events = pointHistoryService.events(eventsResponse);
        Review review = reviewRepository.findReview(events.getReviewId(), user);

        //then
        assertThat(review.getSpecialPlacePoint()).isEqualTo(1);
    }

    @Test
    @DisplayName("이벤트 메시지의 Action이 ADD일 때 이미지 1개 이상 삽입 시 포인트 1점을 획득합니다.")
    void reviewActionADD_Image_Notnull_PlusPoint() {

        //given
        User user = user();
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();


        List<UUID> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add(UUID.randomUUID());
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD,
                reviewId, "테스트", attachedPhotoIds, user.getId(), placeId);
        //when
        ReviewResponse events = pointHistoryService.events(eventsResponse);
        Review review = reviewRepository.findReview(events.getReviewId(), user);

        //then
        assertThat(review.getImagePoint()).isEqualTo(1);
    }

    @Test
    @DisplayName("이벤트 메시지의 Action이 ADD일 때 해당 항목에 일치하는 포인트가 유저의 포인트에 추가됩니다.")
    void reviewActionADD_UserPoint() {

        //given
        User user = user();
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();

        List<UUID> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add(UUID.randomUUID());
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD,
                reviewId, "테스트", attachedPhotoIds, user.getId(), placeId);
        //when
        ReviewResponse events = pointHistoryService.events(eventsResponse);
        Review review = reviewRepository.findReview(events.getReviewId(), user);

        //then
        assertThat(attachedPhotoIds.size()).isEqualTo(1);
        assertThat(review.getImagePoint()).isEqualTo(1);
        assertThat(review.getContentPoint()).isEqualTo(1);
        assertThat(review.getSpecialPlacePoint()).isEqualTo(1);
        assertThat(review.getUser().getCurrentPoint()).isEqualTo(3);
    }

    @Test
    @DisplayName("해당 장소의 두번째 리뷰를 작성한 유저는 특정 장소에서의 리뷰작성에 대한 보너스 포인트를 획득하지 못합니다.")
    void secondReviewADD() {

        //given
        Review firstReviewer = review();
        UUID reviewId = UUID.randomUUID();
        User user2 = userRepository.save(new User("SecondReviewer"));

        //when
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD,
                reviewId, "두번째 사람의 리뷰", attachedPhotoIds, user2.getId(), firstReviewer.getPlaceId());
        ReviewResponse events = pointHistoryService.events(eventsResponse);
        Review secondReviewer = reviewRepository.findReview(events.getReviewId(), user2);

        //then
        assertThat(secondReviewer.getContentPoint()).isEqualTo(1);
        assertThat(attachedPhotoIds.size()).isEqualTo(0);
        assertThat(secondReviewer.getImagePoint()).isEqualTo(0);
        assertThat(secondReviewer.getSpecialPlacePoint()).isEqualTo(0);
        assertThat(user2.getCurrentPoint()).isEqualTo(1);

    }

    @Test
    @DisplayName("유저가 같은 장소에 대한 리뷰를 작성 시 UserCustomException이 발생합니다.")
    void duplicateUserWriteReview() {

        //given
        User user = user();
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        reviewWrite(user, reviewId, placeId, "첫번째 리뷰");

        // when
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse secondReview = new EventsResponse("REVIEW", ReviewAction.ADD,
                reviewId, "두번째 리뷰", attachedPhotoIds, user.getId(), placeId);

        //then
        assertThrows(UserCustomException.class, () -> {
            pointHistoryService.events(secondReview);
        });

    }

    @Test
    @DisplayName("작성된 리뷰에 각 해당사항에 맞게 포인트가 주어졌을때 리뷰를 수정할때 해당사항에 위반할 경우 위반한 항목의 포인트를 차감시킵니다.")
    void reviewActionMOD_MinusPoint() {

        //given
        Review review = review();
        User user = userRepository.findById(review.getUser().getId()).get();

        //when
        List<UUID> attachedPhotoIds = new ArrayList<>();
        int beforeReviewMOD = user.getCurrentPoint();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.MOD,
                review.getId(), "", attachedPhotoIds, review.getUser().getId(), review.getPlaceId());
        pointHistoryService.events(eventsResponse);

        //then
        assertThat(beforeReviewMOD).isEqualTo(2);
        assertThat(user.getCurrentPoint()).isEqualTo(1);
        assertThat(review.getImagePoint()).isEqualTo(0);
        assertThat(attachedPhotoIds.size()).isEqualTo(0);
        assertThat(review.getSpecialPlacePoint()).isEqualTo(1);
        assertThat(review.getContentPoint()).isEqualTo(0);
    }

    @Test
    @DisplayName("작성된 리뷰에 각 해당사항에 알맞지 않아 포인트가 주어지지 않았을때 리뷰를 수정할때 해당사항에 해당할 경우 해당되는 항목의 포인트를 부여합니다.")
    void reviewActionMOD_PlusPoint() {

        //given
        Review review = review();
        User user = userRepository.findById(review.getUser().getId()).get();

        //when
        List<UUID> attachedPhotoIds = new ArrayList<>();
        int beforeReviewMOD = user.getCurrentPoint();
        attachedPhotoIds.add(UUID.randomUUID());
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.MOD,
                review.getId(), "테스트", attachedPhotoIds, review.getUser().getId(), review.getPlaceId());
        pointHistoryService.events(eventsResponse);

        //then
        assertThat(beforeReviewMOD).isEqualTo(2);
        assertThat(user.getCurrentPoint()).isEqualTo(3);
        assertThat(review.getImagePoint()).isEqualTo(1);
        assertThat(attachedPhotoIds.size()).isEqualTo(1);
        assertThat(review.getSpecialPlacePoint()).isEqualTo(1);
        assertThat(review.getContentPoint()).isEqualTo(1);
    }

    @Test
    @DisplayName("작성한 리뷰를 삭제할 경우 부여된 포인트를 회수합니다. ")
    void reviewActionDELETE() {

        //given
        Review review = review();
        User user = review.getUser();
        int beforeDeleteReviewPoint = user.getCurrentPoint();
        List<UUID> attachedPhotoIds = new ArrayList();
        EventsResponse reviewResponse = new EventsResponse("REVIEW", ReviewAction.DELETE,
                review.getId(), review.getContent(), attachedPhotoIds, review.getUser().getId(), review.getPlaceId());

        //when
        pointHistoryService.events(reviewResponse);
        int afterDeleteReviewPoint = user.getCurrentPoint();
        //then
        assertThat(beforeDeleteReviewPoint).isNotEqualTo(afterDeleteReviewPoint);
        assertThat(beforeDeleteReviewPoint).isEqualTo(2);
        assertThat(afterDeleteReviewPoint).isEqualTo(0);

    }

    @Test
    @DisplayName("첫 리뷰 작성한 유저의 리뷰가 삭제되기전에 두번째 리뷰를 단 유저는 특정 장소에서의 리뷰 작성에 대한 포인트를 획득할 수 없습니다.")
    void firstReviewDELETE_Before_SecondReviewWrite() {

        //given
        Review firstReview = review();
        Review secondReview = review();

        //when
        List<UUID> attachedPhotoIds = new ArrayList();
        EventsResponse reviewResponse = new EventsResponse("REVIEW", ReviewAction.DELETE,
                firstReview.getId(), firstReview.getContent(), attachedPhotoIds, firstReview.getUser().getId(), firstReview.getPlaceId());
        int beforeFirstReviewDelete = secondReview.getSpecialPlacePoint();
        pointHistoryService.events(reviewResponse);
        int afterFirstReviewDelete = secondReview.getSpecialPlacePoint();

        //then
        assertThat(beforeFirstReviewDelete).isEqualTo(afterFirstReviewDelete);

    }

    @Test
    @DisplayName("첫 리뷰 작성한 유저의 리뷰가 삭제된 후에 두번째 리뷰를 단 유저는 특정 장소에서의 리뷰 작성에 대한 포인트를 획득할 수 있습니다.")
    void firstReviewDELETE_After_SecondReviewWrite() {

        //given
        Review firstReview = review();

        //when
        List<UUID> attachedPhotoIds = new ArrayList();
        EventsResponse reviewResponse = new EventsResponse("REVIEW", ReviewAction.DELETE,
                firstReview.getId(), firstReview.getContent(), attachedPhotoIds, firstReview.getUser().getId(), firstReview.getPlaceId());
        Review secondReview = review();
        pointHistoryService.events(reviewResponse);
        int secondReviewSpecialPlacePoint = secondReview.getSpecialPlacePoint();

        //then
        assertThat(secondReviewSpecialPlacePoint).isEqualTo(1);
    }

}