package com.example.triple.entity.review;

import com.example.triple.BaseTimeEntity;
import com.example.triple.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(indexes = @Index(name = "review_idx", columnList = "placeId"))
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "reviewId")
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;


    @Type(type = "uuid-char")
    private UUID placeId;

    private String content;

    private int contentPoint;
    private int imagePoint;
    private int specialPlacePoint;
    private int totalPoint;

    private String image;

    public Review(User user, UUID placeId, String content) {
        this.user = user;
        this.placeId = placeId;
        this.content = content;
    }

    public void reviewChange(String content, String image) {
        this.content = content;
        this.image = image;
    }

    public void reviewImage(String image) {
        this.image = image;
    }


    public void increaseOrDecreasePoint(int contentPoint, int imagePoint, int specialPlacePoint, int totalPoint) {
        this.contentPoint = contentPoint;
        this.imagePoint = imagePoint;
        this.specialPlacePoint = specialPlacePoint;
        this.totalPoint = totalPoint;
    }

    public void updateContentPoint(int contentPoint, int imagePoint, int totalPoint) {
        this.contentPoint = contentPoint;
        this.imagePoint = imagePoint;
        this.totalPoint = totalPoint;
    }


}
