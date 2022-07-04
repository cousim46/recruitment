package com.example.triple.repository.review;

import com.example.triple.entity.review.Review;
import com.example.triple.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Query("select r from  Review r where r.id=:reviewId and r.user = :user")
    Review findReview(UUID reviewId, User user);

    void deleteByIdAndUser(UUID reviewId, User user);

    @Query("select count(r) from Review r where r.placeId = :placeId")
    Long countPlace(UUID placeId);

    boolean existsByUserAndPlaceId(User user, UUID placeId);
}
