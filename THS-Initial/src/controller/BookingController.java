package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.*;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class BookingController {

    @FXML
    private DatePicker dateField;
    @FXML
    private TextField timeField;
    @FXML
    private ComboBox<String> specialistNameField;

    @FXML
    public void initialize() {
        // Check if the field was injected correctly 
        if (specialistNameField != null) {
            // Create an ObservableList of specialist names
            ObservableList<String> specialists = FXCollections.observableArrayList(
                    "Dr. Shruti Patel",
                    "Dr. Jal Patel",
                    "Dr. Ketul Patel"
            );

         
            specialistNameField.setItems(specialists);

         
        } else {
            // Handle the case where injection failed 
            System.err.println("Error: specialistNameField was not injected!");
        }
    }

    @FXML
    private void handleBook() {
        LocalDate dateInput = dateField.getValue();
        String timeInput = timeField.getText();
        String specialistName = specialistNameField.getValue();

        if (specialistName == null || dateInput == null || timeInput.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        if (dateInput == null || timeInput.isEmpty() || specialistName.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        try {
            LocalTime time = LocalTime.parse(timeInput);
            LocalDateTime dateTime = LocalDateTime.of(dateInput, time);

            if (!(DataStore.currentUser instanceof Patient)) {
                showAlert("Error", "Only patients can book appointments.");
                return;
            }
            Patient patient = (Patient) DataStore.currentUser;

            Specialist specialist = new Specialist(
                    "S1", specialistName, "pass123", "Dr. " + specialistName,
                    "doctor@mail.com", "999", "Cardiology", "LIC123"
            );

            // Create appointment linked to logged-in patient
            Appointment appointment = new Appointment(patient, specialist, dateTime, "Consultation");
            DataStore.appointments.add(appointment);

            showAlert("Success", "Appointment booked: " + appointment.toString());

        } catch (Exception e) {
            showAlert("Error", "Invalid date or time format. Use YYYY-MM-DD and HH:MM");
        }
    }

    @FXML
    private void openBookingPanel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/booking.fxml"));
            Scene scene = new Scene(loader.load());

            // Get current stage from the button
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Book Appointment");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openBookingPanelLabel(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/booking.fxml"));
            Scene scene = new Scene(loader.load());

            // Get current stage from the label that was clicked
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Book Appointment");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openDashboard(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patient_dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Patient Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openVitalsPanelLabel(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vitals.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Record Vital");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openHealthAnalyticsPanelLabel(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/health_analytics.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Health Analytics");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openPrescriptionsPanelLabel(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/prescriptions.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("My Prescriptions");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
