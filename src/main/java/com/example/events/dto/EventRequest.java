package com.example.events.dto;

import java.time.Instant;

public class EventRequest {

    public String eventId;
    public Instant eventTime;
    public String machineId;
    public String lineId;
    public long durationMs;
    public int defectCount;
}

