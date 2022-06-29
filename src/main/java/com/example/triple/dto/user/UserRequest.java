package com.example.triple.dto.user;

import com.example.triple.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {
    private String username;
    private int point;

    public UserRequest(String username) {
        this.username = username;
    }

    public UserRequest(String username, int point) {
        this.username = username;
        this.point = point;
    }

    public User toEntity() {
        return new User(username);
    }
}
