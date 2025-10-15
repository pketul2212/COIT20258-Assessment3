package model;

import java.time.LocalDateTime;

public class ClinicBooking {

    private String id;
    private String patientId;
    private String hospitalName;
    private LocalDateTime dateTime;
    private String notes;
    private String status;

    public ClinicBooking(Patient p, String hospitalName, LocalDateTime dateTime, String notes) {
        this.patientId = p.getId();
        this.hospitalName = hospitalName;
        this.dateTime = dateTime;
        this.notes = notes;
        this.status = "BOOKED";
    }

    public boolean confirm() {
        this.status = "CONFIRMED";
        return true;
    }

    public boolean cancel() {
        this.status = "CANCELLED";
        return true;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ClinicBooking{" + hospitalName + ", " + dateTime + ", status=" + status + "}";
    }
}
