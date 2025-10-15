package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

public class SpecialistDashboardController {

    @FXML
    private TableView<Appointment> appointmentsTableView;

    @FXML
    private TableColumn<Appointment, String> apptIdColumn;

    @FXML
    private TableColumn<Appointment, String> apptPatientNameColumn;

    @FXML
    private TableColumn<Appointment, String> apptDateTimeColumn;

    @FXML
    private TableColumn<Appointment, String> apptReasonColumn;

    @FXML
    private TableColumn<Appointment, String> apptStatusColumn;

    @FXML
    private TextArea diagnosisTextArea;

    @FXML
    private TextArea treatmentTextArea;

    @FXML
    private TextField medicationField;

    @FXML
    private TextField dosageField;

    @FXML
    private TextField durationField;

    @FXML
    private TextField hospitalNameField;

    @FXML
    private DatePicker externalBookingDatePicker;

    @FXML
    private TextField externalBookingTimeField;

    @FXML
    private TextField externalBookingNotesField;

    @FXML
    private Button createBookingButton;
    // --- End New Fields ---

    @FXML
    private Button completeConsultationButton;

    @FXML
    private Button issuePrescriptionButton;
    @FXML
    private Button startSessionButton;

    @FXML
    private Button endSessionButton;

    @FXML
    private Button recordSessionButton;

    @FXML
    private Label sessionStatusLabel;

    private Specialist currentSpecialist;

    private ObservableList<Appointment> appointmentData = FXCollections.observableArrayList();

    private TelemedicineSession currentSession = null;

    @FXML
    public void initialize() {
        if (!(DataStore.currentUser instanceof Specialist)) {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "Only specialists can access this dashboard.");
            return;
        }
        currentSpecialist = (Specialist) DataStore.currentUser;

        // Set up TableView columns
        apptIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        apptPatientNameColumn.setCellValueFactory(cellData -> {
            Patient p = cellData.getValue().getPatient();
            return new javafx.beans.property.SimpleStringProperty(p != null ? p.getName() : "Unknown");
        });
        apptDateTimeColumn.setCellValueFactory(cellData -> {
            LocalDateTime dt = cellData.getValue().getDateTime();
            String formattedDateTime = (dt != null) ? dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
            return new javafx.beans.property.SimpleStringProperty(formattedDateTime);
        });
        apptReasonColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReason()));
        apptStatusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().toString()));

        // Bind the ObservableList to the TableView
        appointmentsTableView.setItems(appointmentData);

        // Initially disable detail/action buttons until an appointment is selected
        setAllInputFieldsDisabled(true);
        setAllActionButtonsDisabled(true);

        // Add a listener to enable/disable fields/buttons based on selection
        appointmentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isItemSelected = (newSelection != null);
            setAllInputFieldsDisabled(!isItemSelected);
            setDiagnosisTreatmentButtonsState(isItemSelected, newSelection);
            setPrescriptionButtonsState(isItemSelected, newSelection);
            setExternalBookingFieldsState(isItemSelected);
            setTelemedicineSessionUIState(isItemSelected, newSelection);

            if (isItemSelected) {
                // Pre-populate diagnosis/treatment if already recorded
                diagnosisTextArea.setText(newSelection.getDiagnosis() != null ? newSelection.getDiagnosis() : "");
                treatmentTextArea.setText(newSelection.getTreatment() != null ? newSelection.getTreatment() : "");
            } else {
                // Clear all input fields if no selection
                clearAllInputFields();
            }
        });

        loadSpecialistAppointments();
    }

    // --- Helper methods for initialize() to improve readability ---
    private void setAllInputFieldsDisabled(boolean disabled) {
        diagnosisTextArea.setDisable(disabled);
        treatmentTextArea.setDisable(disabled);
        medicationField.setDisable(disabled);
        dosageField.setDisable(disabled);
        durationField.setDisable(disabled);
        hospitalNameField.setDisable(disabled);
        externalBookingDatePicker.setDisable(disabled);
        externalBookingTimeField.setDisable(disabled);
        externalBookingNotesField.setDisable(disabled);
    }

    private void setAllActionButtonsDisabled(boolean disabled) {
        completeConsultationButton.setDisable(disabled);
        issuePrescriptionButton.setDisable(disabled);
        createBookingButton.setDisable(disabled);
        startSessionButton.setDisable(disabled);
        endSessionButton.setDisable(disabled);
        recordSessionButton.setDisable(disabled);
    }

    private void setDiagnosisTreatmentButtonsState(boolean isItemSelected, Appointment appointment) {
        completeConsultationButton.setDisable(!isItemSelected);
        // Could add status checks here if needed
    }

    private void setPrescriptionButtonsState(boolean isItemSelected, Appointment appointment) {
        issuePrescriptionButton.setDisable(!isItemSelected || appointment.getStatus() == AppointmentStatus.COMPLETED);
    }

    private void setExternalBookingFieldsState(boolean isItemSelected) {
        createBookingButton.setDisable(!isItemSelected);
    }

    private void setTelemedicineSessionUIState(boolean isItemSelected, Appointment appointment) {
        startSessionButton.setDisable(!isItemSelected);
        if (isItemSelected && appointment.hasTelemedicineSession()) {
            currentSession = appointment.getTelemedicineSession();
            endSessionButton.setDisable(false);
            recordSessionButton.setDisable(currentSession.getEndTime() == null);
            updateSessionStatusLabel();
        } else {
            currentSession = null;
            endSessionButton.setDisable(true);
            recordSessionButton.setDisable(true);
            sessionStatusLabel.setText("No active session");
        }
    }

    private void clearAllInputFields() {
        diagnosisTextArea.clear();
        treatmentTextArea.clear();
        medicationField.clear();
        dosageField.clear();
        durationField.clear();
        hospitalNameField.clear();
        externalBookingDatePicker.setValue(null);
        externalBookingTimeField.clear();
        externalBookingNotesField.clear();
    }
  
    private void loadSpecialistAppointments() {
        ObservableList<Appointment> filteredAppointments = DataStore.appointments.stream()
                .filter(appointment -> {
                    Specialist spec = appointment.getSpecialist();
                    return spec != null && spec.getSpecialistId().equals(currentSpecialist.getSpecialistId());
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        appointmentData.setAll(filteredAppointments);
    }

    /**
     * Handles the refresh button action.
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        loadSpecialistAppointments();
        appointmentsTableView.getSelectionModel().clearSelection();
    }

    /**
     * Handles the complete consultation button action. Records diagnosis and
     * treatment for the selected appointment.
     */
    @FXML
    private void handleCompleteConsultation(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an appointment to complete.");
            return;
        }

        AppointmentStatus status = selectedAppointment.getStatus();
        if (status != AppointmentStatus.BOOKED && status != AppointmentStatus.RESCHEDULED) {
            showAlert(Alert.AlertType.WARNING, "Invalid Action", "Can only complete appointments that are Booked or Rescheduled. Current status: " + status);
            return;
        }

        String diagnosis = diagnosisTextArea.getText().trim();
        String treatment = treatmentTextArea.getText().trim();

        if (diagnosis.isEmpty() || treatment.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Data", "Please enter both diagnosis and treatment plan.");
            return;
        }

        try {
            currentSpecialist.recordDiagnosis(selectedAppointment, diagnosis, treatment);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Consultation completed for appointment " + selectedAppointment.getId() + ".");
            handleRefresh(new ActionEvent()); // Refresh to show updated status
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to complete consultation: " + e.getMessage());
        }
    }

    /**
     * Handles the issue prescription button action. Creates a new prescription
     * for the selected appointment's patient.
     */
    @FXML
    private void handleIssuePrescription(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an appointment first.");
            return;
        }

        String medication = medicationField.getText().trim();
        String dosage = dosageField.getText().trim();
        String durationStr = durationField.getText().trim();

        if (medication.isEmpty() || dosage.isEmpty() || durationStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Data", "Please fill in all prescription fields (Medication, Dosage, Duration).");
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(durationStr);
            if (duration <= 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Duration must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Duration must be a valid integer.");
            return;
        }

        try {
            Patient patient = selectedAppointment.getPatient();
            if (patient == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not find patient for this appointment.");
                return;
            }

            Prescription newPrescription = currentSpecialist.issuePrescription(patient, medication, dosage, duration);
            // DataStore.prescriptions.add(newPrescription); // Uncomment if global list needed
            showAlert(Alert.AlertType.INFORMATION, "Success", "Prescription issued for " + medication + " to patient " + patient.getName() + ".");
            medicationField.clear();
            dosageField.clear();
            durationField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to issue prescription: " + e.getMessage());
        }
    }

    // --- Handler Method for External Booking ---
    /**
     * Handles the create external booking button action. Creates a new clinic
     * booking for the selected appointment's patient.
     */
    @FXML
    private void handleCreateExternalBooking(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an appointment first.");
            return;
        }

        String hospitalName = hospitalNameField.getText().trim();
        LocalDate bookingDate = externalBookingDatePicker.getValue();
        String timeText = externalBookingTimeField.getText().trim();
        String notes = externalBookingNotesField.getText().trim();

        if (hospitalName.isEmpty() || bookingDate == null || timeText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Data", "Please fill in Hospital Name, Date, and Time for the external booking.");
            return;
        }

        LocalDateTime bookingDateTime;
        try {
            LocalTime bookingTime = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("H:mm"));
            bookingDateTime = LocalDateTime.of(bookingDate, bookingTime);
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Time", "Please enter time in HH:MM format (e.g., 14:30).");
            return;
        }

        try {
            Patient patient = selectedAppointment.getPatient();
            if (patient == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not find patient for this appointment.");
                return;
            }

            ClinicBooking newBooking = currentSpecialist.createClinicBooking(patient, hospitalName, bookingDateTime, notes);
            selectedAppointment.setExternalBooking(newBooking);
            // DataStore.clinicBookings.add(newBooking); // Uncomment if global list needed

            showAlert(Alert.AlertType.INFORMATION, "Success", "External booking created for " + patient.getName() + " at " + hospitalName + ".");
            hospitalNameField.clear();
            externalBookingDatePicker.setValue(null);
            externalBookingTimeField.clear();
            externalBookingNotesField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create external booking: " + e.getMessage());
        }
    }

    // --- Handler Methods for Telemedicine Session ---
    @FXML
    private void handleStartSession(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an appointment first.");
            return;
        }

        if (selectedAppointment.hasTelemedicineSession()) {
            showAlert(Alert.AlertType.WARNING, "Session Exists", "A session already exists for this appointment.");
            return;
        }

        try {
            TelemedicineSession newSession = new TelemedicineSession(selectedAppointment);
            String sessionUrl = newSession.startSession();
            selectedAppointment.setTelemedicineSession(newSession);
            currentSession = newSession;

            endSessionButton.setDisable(false);
            recordSessionButton.setDisable(true); // Cannot record until ended
            updateSessionStatusLabel();

            showAlert(Alert.AlertType.INFORMATION, "Session Started", "Session started. URL: " + sessionUrl);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to start session: " + e.getMessage());
        }
    }

    @FXML
    private void handleEndSession(ActionEvent event) {
        if (currentSession == null) {
            showAlert(Alert.AlertType.WARNING, "No Active Session", "There is no active session to end.");
            return;
        }

        try {
            currentSession.endSession();
            recordSessionButton.setDisable(false); // Now it can be recorded
            updateSessionStatusLabel();
            showAlert(Alert.AlertType.INFORMATION, "Session Ended", "Session has been ended.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to end session: " + e.getMessage());
        }
    }

    @FXML
    private void handleRecordSession(ActionEvent event) {
        if (currentSession == null || currentSession.getEndTime() == null) {
            showAlert(Alert.AlertType.WARNING, "Invalid Action", "Session must be ended before recording.");
            return;
        }

        // Simulate recording to a file path
        String recordingPath = "recordings/session_" + currentSession.getSessionId() + ".mp4";

        try {
            boolean success = currentSession.recordSession(recordingPath);

            if (success) {
                updateSessionStatusLabel();
                showAlert(Alert.AlertType.INFORMATION, "Session Recorded", "Session recorded successfully to: " + recordingPath);
            } else {
                showAlert(Alert.AlertType.ERROR, "Recording Failed", "Failed to record the session.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error during recording: " + e.getMessage());
        }
    }

    private void updateSessionStatusLabel() {
        if (currentSession != null) {
            if (currentSession.getStartTime() != null && currentSession.getEndTime() == null) {
                sessionStatusLabel.setText("Session Active");
            } else if (currentSession.getStartTime() != null && currentSession.getEndTime() != null) {
                if (currentSession.getRecordingPath() != null && !currentSession.getRecordingPath().isEmpty()) {
                    sessionStatusLabel.setText("Session Ended & Recorded");
                } else {
                    sessionStatusLabel.setText("Session Ended (Not Recorded)");
                }
            } else {
                sessionStatusLabel.setText("Session State Unknown");
            }
        } else {
            sessionStatusLabel.setText("No active session");
        }
    }
    // --- End Handler Methods for Telemedicine Session ---

    /**
     * Displays an alert dialog to the user.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- Navigation Methods ---
    @FXML
    public void openSpecialistDashboard(MouseEvent event) {
        handleRefresh(new ActionEvent());
    }

    private void navigateTo(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load the " + title + " view.");
        } catch (ClassCastException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Invalid source for navigation event.");
        }
    }
    // --- End Navigation Methods ---
}
