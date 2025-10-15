package model;

import java.time.LocalDateTime;

public class HistoryRow {
    private final String patientName;
    private final String specialistName;
    private final LocalDateTime dateTime;
    private final String reason;

    public HistoryRow(String patientName, String specialistName, LocalDateTime dateTime, String reason) {
        this.patientName = patientName;
        this.specialistName = specialistName;
        this.dateTime = dateTime;
        this.reason = reason;
    }

    public String getPatientName()    { return patientName; }
    public String getSpecialistName() { return specialistName; }
    public LocalDateTime getDateTime(){ return dateTime; }
    public String getReason()         { return reason; }
}
