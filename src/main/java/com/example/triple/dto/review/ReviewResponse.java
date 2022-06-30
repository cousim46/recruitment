package com.example.triple.dto.review;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor

public class ReviewResponse {
    private UUID reviewId;
    private UUID userId;
    private List<UUID> reviewImage;
    private String content;
}
