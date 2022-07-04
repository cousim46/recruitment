package com.example.triple.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNIQUE_WRITE_REVIEW(HttpStatus.BAD_REQUEST, "장소 당 하나의 리뷰만 작성하실 수 있습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
