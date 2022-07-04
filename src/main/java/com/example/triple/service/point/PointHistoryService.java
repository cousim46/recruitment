package com.example.triple.service.point;

import com.example.triple.dto.events.EventsResponse;
import com.example.triple.dto.point.PointHistoryResponse;
import com.example.triple.dto.review.ReviewAction;
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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointHistoryService {
    private final ReviewService reviewService;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserRepository userRepository;


    public EventsResponse events(EventsResponse eventsResponse) {
        ReviewAction action = eventsResponse.getAction();

        if (action.equals(ReviewAction.ADD)) {
            reviewService.reviewWrite(eventsResponse.getUserId(), eventsResponse.getPlaceId(),
                    eventsResponse.getAttachedPhotoIds(), eventsResponse.getContent());
        }
        if (action.equals(ReviewAction.MOD)) {
            reviewService.reviewUpdate(eventsResponse.getReviewId(), eventsResponse.getUserId(),
                    eventsResponse.getPlaceId(), eventsResponse.getAttachedPhotoIds(),
                    eventsResponse.getContent());
        }
        if (action.equals(ReviewAction.DELETE)) {
            reviewService.reviewDelete(eventsResponse.getReviewId(), eventsResponse.getUserId());
        }
        return eventsResponse;
    }

    @Transactional
    public Slice<PointHistoryResponse> userPointHistory(UUID userId) {
        User user = userRepository.findById(userId).get();
        System.out.println(user.getId());
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createDateTime"));
        Slice<PointHistory> userPointHistory = pointHistoryRepository.findAllByUser(user, pageRequest);
        List<PointHistory> content = userPointHistory.getContent();
        for (PointHistory pointHistory : content) {
            System.out.println("pointHistory = " + pointHistory);
        }
        return userPointHistory.map(userPoint -> new PointHistoryResponse(
                userPoint.getUser().getId(),
                userPoint.getBeforePoint(),
                userPoint.getIncreaseOrDecreasePoint(),
                userPoint.getAfterPoint()
        ));

    }
}
