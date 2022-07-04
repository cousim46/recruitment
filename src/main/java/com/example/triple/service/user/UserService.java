package com.example.triple.service.user;

import com.example.triple.BaseTimeEntity;
import com.example.triple.dto.user.UserRequest;
import com.example.triple.dto.user.UserResponse;
import com.example.triple.entity.user.User;
import com.example.triple.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService extends BaseTimeEntity {

    private final UserRepository userRepository;

    public UserResponse save(UserRequest userRequest) {
        User user = userRequest.toEntity();
        userRepository.save(user);
        log.info("{}의 uuid {}", user.getUsername(), user.getId());
        return new UserResponse(user.getId(), user.getUsername());

    }


}
