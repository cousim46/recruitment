package com.example.triple.controller.point;

import com.example.triple.dto.events.EventsResponse;
import com.example.triple.dto.point.PointHistoryResponse;
import com.example.triple.dto.review.ReviewResponse;
import com.example.triple.service.point.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PointHistoryController {
    private final PointHistoryService pointHistoryService;


    @PostMapping("/events")
    public ResponseEntity<ReviewResponse> events(@Validated @RequestBody EventsResponse eventsResponse) {

        return new ResponseEntity<>(pointHistoryService.events(eventsResponse), HttpStatus.OK);
    }


    @GetMapping("/point-history")
    public ResponseEntity<Slice<PointHistoryResponse>> userPointHistory(@RequestParam(name = "userid") UUID userId) {
        return new ResponseEntity<>(pointHistoryService.userPointHistory(userId), HttpStatus.OK);
    }
}
