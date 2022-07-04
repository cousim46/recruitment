package com.example.triple.service.point;

import com.example.triple.dto.events.EventsResponse;
import com.example.triple.dto.point.PointHistoryResponse;
import com.example.triple.dto.review.ReviewAction;
import com.example.triple.dto.review.ReviewResponse;
import com.example.triple.entity.point.PointHistory;
import com.example.triple.entity.user.User;
import com.example.triple.repository.point.PointHistoryRepository;
import com.example.triple.repository.user.UserRepository;
import com.example.triple.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointHistoryService {
    private final ReviewService reviewService;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserRepository userRepository;


    public ReviewResponse events(EventsResponse eventsResponse) {
        ReviewAction action = eventsResponse.getAction();

        if (action.equals(ReviewAction.ADD)) {
            return reviewService.reviewWrite(eventsResponse.getUserId(), eventsResponse.getPlaceId(),
                    eventsResponse.getAttachedPhotoIds(), eventsResponse.getContent());

        }
        if (action.equals(ReviewAction.MOD)) {
            return reviewService.reviewUpdate(eventsResponse.getReviewId(), eventsResponse.getUserId(),
                    eventsResponse.getAttachedPhotoIds(),
                    eventsResponse.getContent());
        }
        if (action.equals(ReviewAction.DELETE)) {
            reviewService.reviewDelete(eventsResponse.getReviewId(), eventsResponse.getUserId(), eventsResponse.getAttachedPhotoIds());
        }
        return null;
    }

    @Transactional
    public Slice<PointHistoryResponse> userPointHistory(UUID userId) {
        User user = userRepository.findById(userId).get();
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createDateTime"));
        Slice<PointHistory> userPointHistory = pointHistoryRepository.findAllByUser(user, pageRequest);

        return userPointHistory.map(userPoint -> new PointHistoryResponse(
                userPoint.getUser().getId(),
                userPoint.getBeforePoint(),
                userPoint.getIncreaseOrDecreasePoint(),
                userPoint.getAfterPoint()
        ));

    }
}
