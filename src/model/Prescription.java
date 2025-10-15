package model;

import java.time.LocalDate;

public class Prescription {
    private String id;
    private String patientId;
    private String specialistId;
    private String medication;
    private String dosage;
    private int duration;
    private PrescriptionStatus status;
    private LocalDate issueDate;
    private LocalDate expiryDate;

    public Prescription(Patient p, Specialist s, String medication, String dosage, int days) {
        this.patientId = p.getId();
        this.specialistId = s.getId();
        this.medication = medication;
        this.dosage = dosage;
        this.duration = days;
        this.issueDate = LocalDate.now();
        this.expiryDate = issueDate.plusDays(days);
        this.status = PrescriptionStatus.ACTIVE;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getSpecialistId() {
        return specialistId;
    }

    public String getMedication() {
        return medication;
    }

    public String getDosage() {
        return dosage;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean requestRefill() {
        this.status = PrescriptionStatus.REFILL_REQUESTED;
        return true;
    }

    public boolean approveRefill() {
        this.status = PrescriptionStatus.REFILLED;
        return true;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    @Override
    public String toString() {
        return "Prescription{" + medication + ", dosage=" + dosage + ", status=" + status + "}";
    }

    public PrescriptionStatus getStatus() { return status; }
}
