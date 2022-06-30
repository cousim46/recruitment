package com.example.triple.dto.place;

import com.example.triple.entity.place.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRequest {
    private String placeName;

    public Place toEntity() {
        return new Place(placeName);
    }
}
