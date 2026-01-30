package com.example.events.controller;

import com.example.events.dto.BatchIngestResult;
import com.example.events.dto.EventRequest;
import com.example.events.service.EventIngestionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins ="http://localhost:5173")
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventIngestionService ingestionService;

    public EventController(EventIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    /**
     * POST /events/batch
     */
    @PostMapping("/batch")
    public ResponseEntity<BatchIngestResult> ingestBatch(
            @RequestBody(required = false) List<EventRequest> events) {
    	if(events== null) {
    		events = List.of();
    	}

       // BatchIngestResult result = ingestionService.ingest(events);
    	return ResponseEntity.ok(ingestionService.ingest(events));
    }
}
