package com.example.triple.entity.point;

import com.example.triple.BaseTimeEntity;
import com.example.triple.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class PointHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "pointId")
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private int beforePoint;

    private int increaseOrDecreasePoint;
    private int afterPoint;

    public PointHistory(User user, int increaseOrDecreasePoint, int beforePoint, int afterPoint) {
        this.user = user;
        this.increaseOrDecreasePoint = increaseOrDecreasePoint;
        this.beforePoint = beforePoint;
        this.afterPoint = afterPoint;
    }
}
