package com.example.triple.dto.events;

import com.example.triple.dto.review.ReviewAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventsResponse {
    @NotNull(message = "타입이 필요합니다.")
    private String type;
    @NotNull(message = "Action이 필요합니다.")
    private ReviewAction action;
    @NotNull(message = "리뷰ID가 필요합니다.")
    private UUID reviewId;
    private String content;
    private List<UUID> attachedPhotoIds;
    @NotNull(message = "유저ID가 필요합니다.")
    private UUID userId;
    @NotNull(message = "장소ID가 필요합니다.")
    private UUID placeId;
}
