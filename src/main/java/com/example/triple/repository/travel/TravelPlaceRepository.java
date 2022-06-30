package com.example.triple.repository.travel;


import com.example.triple.entity.travel.TravelPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TravelPlaceRepository extends JpaRepository<TravelPlace, UUID> {
}
