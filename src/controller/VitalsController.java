package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DataStore;
import model.Patient;
import model.VitalSign;
import javafx.scene.input.MouseEvent; 
import java.io.IOException;

public class VitalsController {

    @FXML
    private TextField tempField;
    @FXML
    private TextField hrField;
    @FXML
    private TextField bpField;
    

    /**
     * Handles the action when the "Save Vital Signs" button is clicked.
     * Validates input, creates a VitalSign object, and stores it.
     */
    @FXML
    private void handleSaveVitals(ActionEvent event) {
        try {
            String tempText = tempField.getText().trim();
            String hrText = hrField.getText().trim();
            String bpText = bpField.getText().trim();

            if (tempText.isEmpty() || hrText.isEmpty() || bpText.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all required fields (Temperature, Heart Rate, Blood Pressure).");
                return;
            }

            double temperature = Double.parseDouble(tempText);
            int heartRate = Integer.parseInt(hrText);
            String bloodPressure = bpText; 

          
            if (temperature < 30 || temperature > 45) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Temperature seems out of range (30-45°C).");
                return;
            }
            if (heartRate < 30 || heartRate > 200) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input", "Heart Rate seems out of range (30-200 bpm).");
                return;
            }

            // 3. Get current patient
            if (!(DataStore.currentUser instanceof Patient)) {
                showAlert(Alert.AlertType.ERROR, "Access Denied", "Only patients can record vital signs.");
                return;
            }
            Patient patient = (Patient) DataStore.currentUser;

                    VitalSign newVital = patient.recordVitalSigns(temperature, heartRate, bloodPressure);

            // *** ALSO ADD TO GLOBAL DataStore.vitals LIST ***
            if (newVital != null) {
                DataStore.vitals.add(newVital);
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Vital signs recorded successfully.");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for Temperature and Heart Rate.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to record vitals: " + e.getMessage());
            e.printStackTrace(); // Log for debugging
        }
    }

    /**
     * Clears the input fields after successful submission.
     */
    private void clearFields() {
        tempField.clear();
        hrField.clear();
        bpField.clear();
      
    }

    /**
     * Displays an alert dialog to the user.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    @FXML
    private void openBookingPanel(ActionEvent event) {
        navigateTo("/view/booking.fxml", "Book Appointment", event);
    }

    /**
     * Navigates to the Booking panel via Label click (MouseEvent).
     */
    @FXML
    public void openBookingPanelLabel(MouseEvent event) {
        navigateTo("/view/booking.fxml", "Book Appointment", event);
    }

    /**
     * Navigates to the Patient Dashboard (FXML).
     */
    @FXML
    public void openDashboard(MouseEvent event) {
        navigateTo("/view/patient_dashboard.fxml", "Patient Dashboard", event);
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

    private void navigateTo(String fxmlPath, String title, javafx.event.Event event) {
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
        }
    }

}
