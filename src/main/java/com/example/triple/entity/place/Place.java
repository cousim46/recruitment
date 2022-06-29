package com.example.triple.entity.place;

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
public class Place {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column( name = "placeId")
    @Type(type = "uuid-char")
    private UUID id;

    private String placeName;


    public Place(String placeName) {
        this.placeName = placeName;
    }
}
