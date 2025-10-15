
import model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;

public class PrescriptionTest {

    private Patient patient;
    private Specialist specialist;
    private String medication;
    private String dosage;
    private int days;

    @Before
    public void setUp() {
        patient = new Patient("P1", "puser", "ppass", "Patient Name", "p@example.com", "123", "Addr", LocalDate.now());
        specialist = new Specialist("S1", "suser", "spass", "Specialist Name", "s@example.com", "456", "Cardiology", "LIC1");
        medication = "Antibiotic X";
        dosage = "500mg";
        days = 7;
    }

    @Test
    public void testConstructor_InitializesCorrectly() {
        Prescription prescription = new Prescription(patient, specialist, medication, dosage, days);

        assertEquals(patient.getPatientId(), prescription.getPatientId());
        assertEquals(specialist.getSpecialistId(), prescription.getSpecialistId());
        assertEquals(medication, prescription.getMedication());
        assertEquals(dosage, prescription.getDosage());
        assertEquals(days, prescription.getDuration());
        assertEquals(PrescriptionStatus.ACTIVE, prescription.getStatus());
        assertNotNull("Issue date should be set", prescription.getIssueDate());
        assertNotNull("Expiry date should be calculated", prescription.getExpiryDate());

    }

    @Test
    public void testRequestRefill_ChangesStatus() {
        Prescription prescription = new Prescription(patient, specialist, medication, dosage, days);

        boolean result = prescription.requestRefill();

        assertTrue("Request refill should return true", result);
        assertEquals(PrescriptionStatus.REFILL_REQUESTED, prescription.getStatus());
    }

    @Test
    public void testApproveRefill_ChangesStatus() {
        Prescription prescription = new Prescription(patient, specialist, medication, dosage, days);
        prescription.requestRefill();

        boolean result = prescription.approveRefill();

        assertTrue("Approve refill should return true", result);
        assertEquals(PrescriptionStatus.REFILLED, prescription.getStatus());
    }

    @Test
    public void testIsExpired_ReturnsCorrectly() {
        Prescription expiredPrescription = new Prescription(patient, specialist, medication, dosage, -1); // Duration -1 day from issue
        assertTrue("Expired prescription should return true", expiredPrescription.isExpired());

        Prescription activePrescription = new Prescription(patient, specialist, medication, dosage, 1); // Duration 1 day from issue
        assertFalse("Active prescription should return false", activePrescription.isExpired());
    }
}
