package com.example.triple.dto.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class PlaceResponse {

    private UUID placeId;
    private UUID userId;
    private String placeName;
}
