package com.example.triple.controller.point;

import com.example.triple.dto.events.EventsResponse;
import com.example.triple.service.point.PointHistoryService;
import com.example.triple.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointController {
    private final PointHistoryService pointHistoryService;
    private final ReviewService reviewService;


    @PostMapping("/events")
    public void events(@RequestBody EventsResponse eventsResponse) {

        pointHistoryService.events(eventsResponse);
    }


}
