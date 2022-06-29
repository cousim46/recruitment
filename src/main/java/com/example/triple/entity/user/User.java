package com.example.triple.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user_entity")
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "userId")
    @Type(type = "uuid-char")
    private UUID id;
    @Column(columnDefinition = "VARCHAR(165)")
    private String username;
    private int point;

    public User(String username) {
        this.username = username;
    }
}
