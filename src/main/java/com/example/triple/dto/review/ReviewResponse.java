package com.example.triple.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private UUID reviewId;
    private List<UUID> reviewImage;
    private String content;
    private UUID placeId;
}
