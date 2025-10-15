package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Specialist extends UserAccount {

    private String specialistId;
    private String specialty;
    private String licenseNumber;
    private int yearsExperience;
    private double consultationFee;
    private List<LocalDateTime> availableSlots = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();

    public Specialist(String id, String username, String passwordHash, String name, String email, String contact, String specialty, String licenseNumber) {
        super(id, username, passwordHash, name, email, contact, Role.SPECIALIST);
        this.specialistId = id;
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
    }

    public Prescription issuePrescription(Patient p, String medication, String dosage, int days) {
        Prescription pr = new Prescription(p, this, medication, dosage, days);
        p.getPrescriptions().add(pr);
        return pr;
    }

    public void recordDiagnosis(Appointment a, String diagnosis, String treatment) {
        a.complete(diagnosis, treatment);
    }

    public ClinicBooking createClinicBooking(Patient p, String hospitalName, LocalDateTime dt, String notes) {
        return new ClinicBooking(p, hospitalName, dt, notes);
    }

    public List<VitalSign> reviewVitalSigns(Patient p) {
        return p.getVitals();
    }

    public String getSpecialistId() {
        return specialistId;
    }

    @Override
    public String toString() {
        return "Specialist{" + name + ", specialty=" + specialty + "}";
    }
}
