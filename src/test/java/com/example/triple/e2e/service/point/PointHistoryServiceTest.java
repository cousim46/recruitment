package com.example.triple.e2e.service.point;

import com.example.triple.dto.events.EventsResponse;
import com.example.triple.dto.review.ReviewAction;
import com.example.triple.dto.review.ReviewResponse;
import com.example.triple.entity.review.Review;
import com.example.triple.entity.user.User;
import com.example.triple.error.ErrorResponse;
import com.example.triple.repository.review.ReviewRepository;
import com.example.triple.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PointHistoryServiceTest {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    public User user() {
        return userRepository.save(new User("triple"));
    }

    public Review firstReviewer(UUID placeId) {
        //given
        User user = user();
        UUID reviewId = UUID.randomUUID();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD, reviewId,
                "리뷰 작성", attachedPhotoIds, user.getId(), placeId);

        //when
        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();
        return reviewRepository.findReview(responseBody.getReviewId(), user);
    }


    @Test
    @DisplayName("이벤트 메시지의 Action이 ADD일 때 글자수 1글자 이상 포인트 1점을 획득합니다.")
    void reviewActionADD_Content_Notnull_PlusPoint() {
        //given
        User user = user();

        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD, reviewId,
                "e2e테스트", attachedPhotoIds, user.getId(), placeId);

        //when
        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();

        Review review = reviewRepository.findReview(responseBody.getReviewId(), user);

        //then

        assertThat(review.getContentPoint()).isEqualTo(1);
    }

    @Test
    @DisplayName("이벤트 메시지의 Action이 ADD일 때 이미지 1개이상 삽입 시 포인트 1점을 획득합니다.")
    void reviewActionADD_Image_Notnull_PlusPoint() {
        //given
        User user = user();
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add(UUID.randomUUID());
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD, reviewId,
                "", attachedPhotoIds, user.getId(), placeId);
        System.out.println(user.getId());
        //when
        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();

        Review review = reviewRepository.findReview(responseBody.getReviewId(), user);
        //then

        assertThat(review.getImagePoint()).isEqualTo(1);
    }

    @Test
    @DisplayName("이벤트 메시지의 Action이 ADD일 때 특정 장소에서 첫번째로 리뷰를 작성 시 포인트 1점을 획득합니다.")
    void reviewActionADD_First_SpecialReview() {
        //given
        User user = user();
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD, reviewId,
                "", attachedPhotoIds, user.getId(), placeId);
        System.out.println(user.getId());
        //when
        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();
        Review review = reviewRepository.findReview(responseBody.getReviewId(), user);
        //then

        assertThat(review.getSpecialPlacePoint()).isEqualTo(1);
    }

    @Test
    @DisplayName("이벤트 메시지의 Action이 ADD일 때 해당 항목에 일치하는 포인트가 유저의 포인트에 추가됩니다.")
    void reviewActionADD_UserPoint() {
        //given
        User user = user();
        int beforeReview = user.getCurrentPoint();
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD, reviewId,
                "리뷰 작성", attachedPhotoIds, user.getId(), placeId);

        //when
        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();
        Review review = reviewRepository.findReview(responseBody.getReviewId(), user);
        //then
        User afterReviewUser = userRepository.findById(review.getUser().getId()).get();
        assertThat(beforeReview).isNotEqualTo(afterReviewUser.getCurrentPoint());
        assertThat(afterReviewUser.getCurrentPoint()).isEqualTo(2);
        assertThat(review.getSpecialPlacePoint()).isEqualTo(1);
        assertThat(review.getContentPoint()).isEqualTo(1);
        assertThat(review.getImagePoint()).isEqualTo(0);
        assertThat(attachedPhotoIds.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("유저가 같은 장소에 대한 리뷰를 작성 시 UserCustomException이 발생합니다.")
    void duplicateUserWriteReview() {
        //given
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        Review firstReviewer = firstReviewer(placeId);
        User user = firstReviewer.getUser();

        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD, reviewId,
                "리뷰 작성", attachedPhotoIds, user.getId(), placeId);

        //when
        ErrorResponse errorResponse = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ErrorResponse.class).returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo("장소 당 하나의 리뷰만 작성하실 수 있습니다.");
        assertThat(errorResponse.getCode()).isEqualTo("UNIQUE_WRITE_REVIEW");

    }

    @Test
    @DisplayName("해당 장소의 두번째 리뷰를 작성한 유저는 특정 장소에서의 리뷰작성에 대한 보너스 포인트를 획득하지 못합니다.")
    void secondReviewADD() {
        //given
        UUID reviewId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        Review firstReviewer = firstReviewer(placeId);

        User secondUser = user();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.ADD, reviewId,
                "리뷰 작성", attachedPhotoIds, secondUser.getId(), placeId);

        //when
        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();
        Review secondReviewer = reviewRepository.findReview(responseBody.getReviewId(), secondUser);

        //then
        assertThat(firstReviewer.getSpecialPlacePoint()).isEqualTo(1);
        assertThat(secondReviewer.getSpecialPlacePoint()).isEqualTo(0);

    }

    @Test
    @DisplayName("작성된 리뷰에 각 해당사항에 맞게 포인트가 주어졌을때 리뷰를 수정할때 해당사항에 위반할 경우 위반한 항목의 포인트를 차감시킵니다.")
    void reviewActionMOD_MinusPoint() {
        //given
        UUID placeId = UUID.randomUUID();
        Review firstReviewer = firstReviewer(placeId);
        User user = userRepository.findById(firstReviewer.getUser().getId()).get();
        int beforePoint = user.getCurrentPoint();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.MOD, firstReviewer.getId(),
                "", attachedPhotoIds, user.getId(), placeId);

        //when
        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();
        Review review = reviewRepository.findReview(responseBody.getReviewId(), user);
        User afterUser = userRepository.findById(review.getUser().getId()).get();

        //then
        assertThat(review.getContentPoint()).isEqualTo(0);
        assertThat(beforePoint).isNotEqualTo(afterUser.getCurrentPoint());
    }

    @Test
    @DisplayName("작성된 리뷰에 각 해당사항에 알맞지 않아 포인트가 주어지지 않았을때 리뷰를 수정할때 해당사항에 해당할 경우 해당되는 항목의 포인트를 부여합니다.")
    void reviewActionMOD_PlusPoint() {
        UUID placeId = UUID.randomUUID();
        Review firstReviewer = firstReviewer(placeId);
        User user = userRepository.findById(firstReviewer.getUser().getId()).get();
        int beforePoint = user.getCurrentPoint();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        attachedPhotoIds.add(UUID.randomUUID());
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.MOD, firstReviewer.getId(),
                "asd", attachedPhotoIds, user.getId(), placeId);

        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();
        Review review = reviewRepository.findReview(responseBody.getReviewId(), user);
        User afterUser = userRepository.findById(review.getUser().getId()).get();

        assertThat(beforePoint).isLessThan(afterUser.getCurrentPoint());
        assertThat(attachedPhotoIds.size()).isEqualTo(1);
        assertThat(review.getImagePoint()).isEqualTo(1);

    }

    @Test
    @DisplayName("작성한 리뷰를 삭제할 경우 부여된 포인트를 회수합니다. ")
    void reviewActionDELETE() {
        //given
        UUID placeId = UUID.randomUUID();
        Review review = firstReviewer(placeId);
        User user = userRepository.findById(review.getUser().getId()).get();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.DELETE, review.getId(),
                "리뷰 작성", attachedPhotoIds, user.getId(), placeId);
        //when
        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();
        User afterDeleteReviewUser = userRepository.findById(user.getId()).get();

        //then
        assertThat(responseBody).isNull();
        assertThat(afterDeleteReviewUser.getCurrentPoint()).isEqualTo(0);


    }

    @Test
    @DisplayName("첫 리뷰 작성한 유저의 리뷰가 삭제되기전에 두번째 리뷰를 단 유저는 특정 장소에서의 리뷰 작성에 대한 포인트를 획득할 수 없습니다.")
    void firstReviewDELETE_Before_SecondReviewWrite() {
        //given
        UUID placeId = UUID.randomUUID();
        Review firstReviewer = firstReviewer(placeId);
        User firstUser = userRepository.findById(firstReviewer.getUser().getId()).get();
        Review secondReview = firstReviewer(placeId);
        User secondUser = userRepository.findById(secondReview.getUser().getId()).get();


        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.DELETE, firstReviewer.getId(),
                "리뷰 작성", attachedPhotoIds, firstUser.getId(), placeId);

        //when
        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();
        Review firstDeleteAfter = reviewRepository.findReview(secondReview.getId(), secondUser);

        //then
        assertThat(firstDeleteAfter.getSpecialPlacePoint()).isEqualTo(0);


    }

    @Test
    @DisplayName("첫 리뷰 작성한 유저의 리뷰가 삭제된 후에 두번째 리뷰를 단 유저는 특정 장소에서의 리뷰 작성에 대한 포인트를 획득할 수 있습니다.")
    void firstReviewDELETE_After_SecondReviewWrite() {

        //given
        UUID placeId = UUID.randomUUID();
        Review firstReviewe = firstReviewer(placeId);
        User firstUser = userRepository.findById(firstReviewe.getUser().getId()).get();
        List<UUID> attachedPhotoIds = new ArrayList<>();
        EventsResponse eventsResponse = new EventsResponse("REVIEW", ReviewAction.DELETE, firstReviewe.getId(),
                "리뷰 작성", attachedPhotoIds, firstUser.getId(), placeId);

        //when
        ReviewResponse responseBody = webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyValue(eventsResponse)
                .exchange().expectBody(ReviewResponse.class).returnResult().getResponseBody();

        Review secondReview = firstReviewer(placeId);

        //then
        assertThat(secondReview.getSpecialPlacePoint()).isEqualTo(1);


    }


}