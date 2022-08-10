package com.example.walgwalg_front_android.home;

public class RecordData {
    private String startTime, recordStep, recordDistance, recordCalorie;

    public RecordData(String startTime, String recordStep, String recordDistance, String recordCalorie) {
        this.startTime = startTime;
        this.recordStep = recordStep;
        this.recordDistance = recordDistance;
        this.recordCalorie = recordCalorie;
    }

    public RecordData() {
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRecordStep() {
        return recordStep;
    }

    public void setRecordStep(String recordStep) {
        this.recordStep = recordStep;
    }

    public String getRecordDistance() {
        return recordDistance;
    }

    public void setRecordDistance(String recordDistance) {
        this.recordDistance = recordDistance;
    }

    public String getRecordCalorie() {
        return recordCalorie;
    }

    public void setRecordCalorie(String recordCalorie) {
        this.recordCalorie = recordCalorie;
    }
}
