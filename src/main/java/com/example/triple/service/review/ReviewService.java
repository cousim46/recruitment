package com.example.triple.service.review;

import com.example.triple.dto.review.ReviewResponse;
import com.example.triple.entity.point.PointHistory;
import com.example.triple.entity.review.Review;
import com.example.triple.entity.user.User;
import com.example.triple.error.ErrorCode;
import com.example.triple.error.UserCustomException;
import com.example.triple.repository.point.PointHistoryRepository;
import com.example.triple.repository.review.ReviewRepository;
import com.example.triple.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;


    @Transactional
    public ReviewResponse reviewWrite(UUID userId,
                                      UUID placeId,
                                      List<UUID> reviewImages,
                                      String content
    ) {
        User user = userRepository.findById(userId).get();
        if (reviewRepository.existsByUserAndPlaceId(user, placeId)) {
            throw new UserCustomException(ErrorCode.UNIQUE_WRITE_REVIEW);
        }

        int beforePoint = user.getCurrentPoint();
        Review review = new Review(user, placeId, content);
        List<UUID> attachedPhotoIds = reviewImages;
        int reviewImageCnt = attachedPhotoIds.size();
        Long placeCnt = reviewRepository.countPlace(placeId);
        user.givePoint(review, reviewImageCnt, placeCnt);
        int afterPoint = user.getCurrentPoint();
        String image = "";
        for (int i = 0; i < attachedPhotoIds.size(); i++) {
            image += attachedPhotoIds.get(i);
            if (i != attachedPhotoIds.size() - 1) {
                image += ",";
            }
        }
        review.reviewImage(image);
        reviewRepository.save(review);
        pointHistoryRepository.save(new PointHistory(user, beforePoint, review.getTotalPoint(), afterPoint));


        return new ReviewResponse(review.getId(), attachedPhotoIds, review.getContent(), review.getPlaceId());
    }


    /*
    리뷰 수정, 삭제, 그리고 Post events 되는지 확인
     */
    @Transactional
    public ReviewResponse reviewUpdate(UUID reviewId,
                                       UUID userId,
                                       List<UUID> reviewImages,
                                       String content) {

        User user = userRepository.findById(userId).get();
        Review review = reviewRepository.findReview(reviewId, user);
        List<UUID> attachedPhotoIds = reviewImages;
        String image = "";

        for (int i = 0; i < attachedPhotoIds.size(); i++) {
            image += attachedPhotoIds.get(i);
            if (i != attachedPhotoIds.size() - 1) {
                image += ",";
            }
        }
        int contentPoint = review.getContentPoint();
        int imagePoint = review.getImagePoint();
        int totalPoint = review.getTotalPoint();
        int currentPoint = user.getCurrentPoint();

        if (contentPoint == 0 && content.length() > 0) {
            currentPoint++;
            contentPoint++;
            totalPoint++;
        } else if (contentPoint == 1 && content.length() == 0) {
            currentPoint--;
            contentPoint--;
            totalPoint--;
        }
        if (imagePoint == 0 && attachedPhotoIds.size() > 0) {
            currentPoint++;
            imagePoint++;
            totalPoint++;
        } else if (imagePoint == 1 && attachedPhotoIds.size() == 0) {
            currentPoint--;
            imagePoint--;
            totalPoint--;
        }
        review.updateContentPoint(contentPoint, imagePoint, totalPoint);
        int beforePoint = user.getCurrentPoint();

        user.updateCurrentPoint(currentPoint);
        int afterPoint = user.getCurrentPoint();
        review.reviewChange(content, image);
        pointHistoryRepository.save(new PointHistory(user, totalPoint - beforePoint, beforePoint, afterPoint));


        return new ReviewResponse(review.getId(), attachedPhotoIds, review.getContent(), review.getPlaceId());
    }

    @Transactional
    public ReviewResponse reviewDelete(UUID reviewId, UUID userId, List<UUID> attachedPhotoIds) {
        User user = userRepository.findById(userId).get();
        Review review = reviewRepository.findReview(reviewId, user);
        int beforePoint = user.getCurrentPoint();
        int totalPoint = review.getTotalPoint();
        user.decreasePoint(totalPoint);
        int afterPoint = user.getCurrentPoint();
        reviewRepository.deleteByIdAndUser(reviewId, user);
        pointHistoryRepository.save(new PointHistory(user, -beforePoint, beforePoint, afterPoint));


        return new ReviewResponse(review.getId(), attachedPhotoIds, review.getContent(), review.getPlaceId());
    }
}
