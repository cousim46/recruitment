package com.example.triple.dto.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointHistoryResponse {
    private UUID userId;
    private int beforePoint;
    private int increaseOrDecreasePoint;
    private int afterPoint;
}
