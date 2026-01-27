package com.example.events.service;

import com.example.events.dto.*;
import com.example.events.models.MachineEvent;
import com.example.events.repository.MachineEventRepository;
import com.example.events.util.PayloadHashUtil;
import com.example.events.util.RejectionReason;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class EventIngestionService {

    private final MachineEventRepository repository;

    public EventIngestionService(MachineEventRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BatchIngestResult ingest(List<EventRequest> requests) {

        BatchIngestResult result = new BatchIngestResult();
        Instant now = Instant.now();

        for (EventRequest req : requests) {

            // 1️ Validation
            if (req.durationMs < 0 || req.durationMs > Duration.ofHours(6).toMillis()) {
                result.rejected++;
                result.rejections.add(
                    new BatchIngestResult.Rejection(req.eventId, RejectionReason.INVALID_DURATION.name())
                );
                continue;
            }

            if (req.eventTime.isAfter(now.plus(Duration.ofMinutes(15)))) {
                result.rejected++;
                result.rejections.add(
                    new BatchIngestResult.Rejection(req.eventId, RejectionReason.FUTURE_EVENT_TIME.name())
                );
                continue;
            }

            // 2️ Payload hash (excluding receivedTime)
            String payloadHash = PayloadHashUtil.hash(
                req.eventId +
                req.eventTime +
                req.machineId +
                req.lineId +
                req.durationMs +
                req.defectCount
            );

            // 3️ Thread-safe fetch
            Optional<MachineEvent> existingOpt =
                repository.findByIdForUpdate(req.eventId);

            if (existingOpt.isEmpty()) {
                // 4️ New event
                MachineEvent event = toEntity(req, payloadHash, now);
                repository.save(event);
                result.accepted++;
                continue;
            }

            MachineEvent existing = existingOpt.get();

            // 5️ Identical payload → dedupe
            if (existing.getPayloadHash().equals(payloadHash)) {
                result.deduped++;
                continue;
            }

            // 6️ Different payload → update only if newer receivedTime
            if (now.isAfter(existing.getReceivedTime())) {
                updateEntity(existing, req, payloadHash, now);
                result.updated++;
            } else {
                result.deduped++;
            }
        }

        return result;
    }

    private MachineEvent toEntity(EventRequest req, String hash, Instant receivedTime) {
        MachineEvent e = new MachineEvent();
        e.setEventId(req.eventId);
        e.setEventTime(req.eventTime);
        e.setReceivedTime(receivedTime);
        e.setMachineId(req.machineId);
        e.setLineId(req.lineId);
        e.setDurationMs(req.durationMs);
        e.setDefectCount(req.defectCount);
        e.setPayloadHash(hash);
        return e;
    }

    private void updateEntity(MachineEvent e, EventRequest req, String hash, Instant receivedTime) {
        e.setEventTime(req.eventTime);
        e.setMachineId(req.machineId);
        e.setLineId(req.lineId);
        e.setDurationMs(req.durationMs);
        e.setDefectCount(req.defectCount);
        e.setPayloadHash(hash);
        e.setReceivedTime(receivedTime);
    }
}
