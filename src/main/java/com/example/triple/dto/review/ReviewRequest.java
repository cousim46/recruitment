package com.example.triple.dto.review;

import com.example.triple.dto.picture.ReviewImageRequest;
import com.example.triple.dto.place.PlaceRequest;
import com.example.triple.entity.place.Place;
import com.example.triple.entity.review.Review;
import com.example.triple.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
public class ReviewRequest {
    private UUID userId;
    private String content;
    private List<PlaceRequest> reviewImage;
    private PlaceRequest placeRequest;

    public Review toEntity(User user, Place place) {
        return new Review(user,place);
    }

}
