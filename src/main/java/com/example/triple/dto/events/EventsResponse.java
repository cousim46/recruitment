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
    @NotNull
    private String type;
    @NotNull
    private ReviewAction action;
    @NotNull
    private UUID reviewId;
    private String content;
    private List<UUID> attachedPhotoIds;
    @NotNull
    private UUID userId;
    @NotNull
    private UUID placeId;
}
