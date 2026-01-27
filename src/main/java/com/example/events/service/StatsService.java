package com.example.events.service;

import com.example.events.dto.StatsResponse;
import com.example.events.repository.MachineEventRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class StatsService {

    private final MachineEventRepository repository;

    public StatsService(MachineEventRepository repository) {
        this.repository = repository;
    }

    public StatsResponse getStats(String machineId, Instant start, Instant end) {

        StatsResponse response = new StatsResponse();
        response.machineId = machineId;
        response.start = start;
        response.end = end;

        long eventsCount =
                repository.countByMachineIdAndEventTimeGreaterThanEqualAndEventTimeLessThan(
                        machineId, start, end);

        long defectsCount =
                repository.sumDefects(machineId, start, end);

        double windowHours =
                Duration.between(start, end).toSeconds() / 3600.0;

        double avgDefectRate =
                windowHours == 0 ? 0.0 : defectsCount / windowHours;

        response.eventsCount = eventsCount;
        response.defectsCount = defectsCount;
        response.avgDefectRate = avgDefectRate;
        response.status = avgDefectRate < 2.0 ? "Healthy" : "Warning";

        return response;
    }
}
