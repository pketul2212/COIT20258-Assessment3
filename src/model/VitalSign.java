package model;

import java.time.LocalDateTime;

public class VitalSign {
    private String id;
    private String patientId;
    private LocalDateTime timestamp;
    private double temperature;
    private int heartRate;
    private String bloodPressure;
    private int respirationRate;
    private double oxygenSaturation;

    public VitalSign(String patientId, double temperature, int heartRate, String bp) {
        this.patientId = patientId;
        this.timestamp = LocalDateTime.now();
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.bloodPressure = bp;
    }

    public boolean isAbnormal() {
        return (temperature > 39 || heartRate > 120);
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public int getRespirationRate() {
        return respirationRate;
    }

    public double getOxygenSaturation() {
        return oxygenSaturation;
    }

    @Override
    public String toString() {
        return "VitalSign{" + timestamp + ", Temp=" + temperature + ", HR=" + heartRate + ", BP=" + bloodPressure + "}";
    }
}
