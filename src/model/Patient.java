package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Patient extends UserAccount {

    private String patientId;
    private String address;
    private LocalDate dob;
    private String medicalHistory;
    private String emergencyContact;
    private String insuranceDetails;
    private List<Appointment> appointments = new ArrayList<>();
    private List<VitalSign> vitals = new ArrayList<>();
    private List<Prescription> prescriptions = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();

    public Patient(String id, String username, String passwordHash, String name, String email, String contact, String address, LocalDate dob) {
        super(id, username, passwordHash, name, email, contact, Role.PATIENT);
        this.patientId = id;
        this.address = address;
        this.dob = dob;
    }

    public Appointment bookAppointment(Specialist specialist, LocalDateTime dateTime, String reason) {
        Appointment a = new Appointment(this, specialist, dateTime, reason);
        appointments.add(a);
        return a;
    }

    public boolean requestRefill(Prescription p) {
        p.requestRefill();
        return true;
    }

    public VitalSign recordVitalSigns(double temperature, int heartRate, String bp) {
        VitalSign v = new VitalSign(patientId, temperature, heartRate, bp);
        vitals.add(v);
        return v;
    }

    public void receiveNotification(Notification n) {
        notifications.add(n);
    }

    public String viewMedicalHistory() {
        return medicalHistory;
    }

    @Override
    public String toString() {
        return "Patient{" + name + ", username=" + username + "}";
    }

    public String getPatientId() {
        return patientId;
    }

    public String generateHealthReport() {
        if (this.vitals == null || this.vitals.isEmpty()) {
            return "No vital signs recorded for analysis.";
        }

        StringBuilder report = new StringBuilder();
        report.append("=== Health Analytics Report for ").append(this.getName()).append(" ===\n");

        double avgTemp = this.vitals.stream()
                .mapToDouble(VitalSign::getTemperature)
                .average()
                .orElse(0.0);
        report.append("Average Temperature: ").append(String.format("%.2f", avgTemp)).append(" °C\n");

        long abnormalCount = this.vitals.stream()
                .filter(VitalSign::isAbnormal)
                .count();
        report.append("Number of Abnormal Readings: ").append(abnormalCount).append("\n");

        report.append("Recent Vitals:\n");

        int startIndex = Math.max(0, this.vitals.size() - 3);
        for (int i = startIndex; i < this.vitals.size(); i++) {
            report.append("  - ").append(this.vitals.get(i).toString()).append("\n");
        }

        return report.toString();
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<VitalSign> getVitals() {
        return vitals;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }
}
