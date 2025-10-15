package model;

import java.time.LocalDateTime;
import java.util.UUID; 

public class TelemedicineSession {

    private String sessionId;
    private String appointmentId;
    private String sessionUrl; 
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String recordingPath; 
    private String sessionNotes;
    private int participantCount;

    // Constructor
    public TelemedicineSession(Appointment appointment) {
        this.sessionId = UUID.randomUUID().toString();
        this.appointmentId = appointment.getId(); 
        this.sessionUrl = "https://ths-session.example.com/" + this.sessionId;
        this.startTime = null; 
        this.endTime = null;  
        this.recordingPath = null; 
        this.sessionNotes = "";
        this.participantCount = 0;
    }

    // Methods to manage session state
    public String startSession() {
        if (this.startTime == null) {
            this.startTime = LocalDateTime.now();
            this.participantCount = 2; 
            System.out.println("Telemedicine Session started: " + this.sessionId);
            return this.sessionUrl; 
        } else {
            System.out.println("Session already started.");
            return this.sessionUrl; 
        }
    }

    public void endSession() {
        if (this.startTime != null && this.endTime == null) {
            this.endTime = LocalDateTime.now();
            this.participantCount = 0;
            System.out.println("Telemedicine Session ended: " + this.sessionId);
        } else {
            System.out.println("Session not started or already ended.");
        }
    }

    public boolean recordSession(String filePath) {
        if (this.startTime != null && this.endTime != null) {
            this.recordingPath = filePath;
            System.out.println("Session recorded to: " + this.recordingPath);
            return true;
        } else {
            System.out.println("Cannot record session: not started or not ended.");
            return false;
        }
    }

    // Getters (essential for UI and logic)
    public String getSessionId() {
        return sessionId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getSessionUrl() {
        return sessionUrl;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getRecordingPath() {
        return recordingPath;
    }

    public String getSessionNotes() {
        return sessionNotes;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setSessionNotes(String sessionNotes) {
        this.sessionNotes = sessionNotes;
    }

    @Override
    public String toString() {
        return "TelemedicineSession{"
                + "id='" + sessionId + '\''
                + ", url='" + sessionUrl + '\''
                + ", started=" + startTime
                + ", ended=" + endTime
                + ", recordedPath='" + recordingPath + '\''
                + '}';
    }
}
