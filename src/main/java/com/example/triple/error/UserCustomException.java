package com.example.triple.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
