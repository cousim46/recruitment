package com.example.triple.repository.place;

import com.example.triple.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PlaceRepository extends JpaRepository<Place, UUID> {

    boolean existsByPlaceName(String placeName);

    Place findByPlaceName(String placeName);
}
