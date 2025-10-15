package model;

import java.time.LocalDateTime;

public class Appointment {

    private String id;
    private LocalDateTime dateTime;
    private int duration;
    private AppointmentStatus status;
    private String reason;
    private String diagnosis;
    private String treatment;
    private String notes;
    private Patient patient;
    private Specialist specialist;
    private ClinicBooking externalBooking;
    private TelemedicineSession telemedicineSession; 
    
    public Appointment(Patient patient, Specialist specialist, LocalDateTime dateTime, String reason) {
        this.patient = patient;
        this.specialist = specialist;
        this.dateTime = dateTime;
        this.reason = reason;
        this.status = AppointmentStatus.BOOKED;
    }
    

    public boolean reschedule(LocalDateTime newDateTime) {
        this.dateTime = newDateTime;
        this.status = AppointmentStatus.RESCHEDULED;
        return true;
    }

    public void cancel(String reason) {
        this.status = AppointmentStatus.CANCELLED;
        this.notes = reason;
    }

    public void complete(String diagnosis, String treatment) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.status = AppointmentStatus.COMPLETED;
    }

    public void addNotes(String notes) {
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Patient getPatient() {
        return patient;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public String getReason() {
        return reason;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getNotes() {
        return notes;
    }

    public ClinicBooking getExternalBooking() {
        return externalBooking;
    }

    public int getDuration() {
        return duration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }

    public void setExternalBooking(ClinicBooking externalBooking) {
        this.externalBooking = externalBooking;
    }


    public TelemedicineSession getTelemedicineSession() {
        return this.telemedicineSession;
    }

    public void setTelemedicineSession(TelemedicineSession session) {
        this.telemedicineSession = session;
    }

    public boolean hasTelemedicineSession() {
        return this.telemedicineSession != null;
    }

    @Override
    public String toString() {
        return "Appointment{" + dateTime + " with " + specialist.getName() + "}";
    }

    public AppointmentStatus getStatus() {
        return status;
    }
    

   
    
}
