package com.example.events.controller;

import com.example.events.dto.StatsResponse;
import com.example.events.service.StatsService;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.events.dto.LineDefectStats;

import java.time.Instant;
@CrossOrigin(origins ="http://localhost:5173")
@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public ResponseEntity<StatsResponse> getStats(
            @RequestParam String machineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end
    ) {
        return ResponseEntity.ok(
                statsService.getStats(machineId, start, end)
        );
    }
    @GetMapping("/stats/top-defect-lines")
    public List<LineDefectStats> topDefectLines(
            @RequestParam Instant start,
            @RequestParam Instant end,
            @RequestParam(defaultValue = "3") int limit
    ) {
        return statsService.topDefectLines(start, end, limit);
    }

    
}
