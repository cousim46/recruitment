package com.example.triple.entity.user;

import com.example.triple.entity.review.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "userId")
    @Type(type = "uuid-char")
    private UUID id;
    @Column(columnDefinition = "VARCHAR(165)")
    private String username;
    private int currentPoint;

    public User(String username) {
        this.username = username;
    }

    public void updateCurrentPoint(int currentPoint) {
        this.currentPoint = currentPoint;
    }

    public void givePoint(Review review, int imageCount, Long placeCount) {
        int contentPoint = 0;
        int imagePoint = 0;
        int specialPlacePoint = 0;
        int totalPoint = 0;
        if (review.getContent().length() >= 1) {
            contentPoint = 1;
        }
        if (imageCount >= 1) {
            imagePoint = 1;
        }
        if (placeCount == 0) {
            specialPlacePoint = 1;
        }
        totalPoint = contentPoint + imagePoint + specialPlacePoint;
        review.increaseOrDecreasePoint(contentPoint, imagePoint, specialPlacePoint, totalPoint);

        this.currentPoint = this.currentPoint + totalPoint;
    }

    public void decreasePoint(int point) {
        this.currentPoint -= point;
    }
}
