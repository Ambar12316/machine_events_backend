package com.example.events.dto;

public class LineDefectStats {

    private String lineId;
    private Long totalDefects;

    public LineDefectStats(String lineId, Long totalDefects) {
        this.lineId = lineId;
        this.totalDefects = totalDefects;
    }

    public String getLineId() {
        return lineId;
    }

    public Long getTotalDefects() {
        return totalDefects;
    }
}

