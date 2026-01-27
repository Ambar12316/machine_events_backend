package com.example.events.models;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "machine_event")
public class MachineEvent {

    @Id
    private String eventId;

    private Instant eventTime;
    private Instant receivedTime;
    private String machineId;
    private String lineId;
    private long durationMs;
    private int defectCount;
    private String payloadHash;

    public MachineEvent() {
        // JPA requires default constructor
    }

//    // -------- GETTERS --------
//    public String getEventId() {
//        return eventId;
//    }
//
//    public Instant getEventTime() {
//        return eventTime;
//    }
//
//    public Instant getReceivedTime() {
//        return receivedTime;
//    }
//
//    public String getMachineId() {
//        return machineId;
//    }
//
//    public String getLineId() {
//        return lineId;
//    }
//
//    public long getDurationMs() {
//        return durationMs;
//    }
//
//    public int getDefectCount() {
//        return defectCount;
//    }
//
//    public String getPayloadHash() {
//        return payloadHash;
//    }

//    // -------- SETTERS --------
//    public void setEventId(String eventId) {
//        this.eventId = eventId;
//    }
//
//    public void setEventTime(Instant eventTime) {
//        this.eventTime = eventTime;
//    }
//
//    public void setReceivedTime(Instant receivedTime) {
//        this.receivedTime = receivedTime;
//    }
//
//    public void setMachineId(String machineId) {
//        this.machineId = machineId;
//    }
//
//    public void setLineId(String lineId) {
//        this.lineId = lineId;
//    }
//
//    public void setDurationMs(long durationMs) {
//        this.durationMs = durationMs;
//    }
//
//    public void setDefectCount(int defectCount) {
//        this.defectCount = defectCount;
//    }
//
//    public void setPayloadHash(String payloadHash) {
//        this.payloadHash = payloadHash;
//    }
}
