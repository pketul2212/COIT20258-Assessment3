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
import java.time.LocalDateTime;

import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty; 

public class StaffDashboardController {

    @FXML
    private TextField patientIdSearchField;

    @FXML
    private TableView<Appointment> appointmentsTableView;

    @FXML
    private TableColumn<Appointment, String> idColumn;

    @FXML
    private TableColumn<Appointment, String> patientNameColumn;

    @FXML
    private TableColumn<Appointment, String> dateTimeColumn; 

    @FXML
    private TableColumn<Appointment, String> specialistColumn;

    @FXML
    private TableColumn<Appointment, String> statusColumn;

    @FXML
    private Button modifyButton;

    @FXML
    private Button cancelButton;

   
    private HealthcareStaff currentStaff;


    private ObservableList<Appointment> appointmentData = FXCollections.observableArrayList();

  
    @FXML
    public void initialize() {
        
        if (!(DataStore.currentUser instanceof HealthcareStaff)) {
            
            showAlert(Alert.AlertType.ERROR, "Access Denied", "Only healthcare staff can access this dashboard.");
            
            return;
        }
        currentStaff = (HealthcareStaff) DataStore.currentUser;

       
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
       
        patientNameColumn.setCellValueFactory(cellData -> {
            Appointment appt = cellData.getValue();
            
            Patient p = appt.getPatient(); 
            return new SimpleStringProperty(p != null ? p.getName() : "Unknown Patient");
        });
        

      
        dateTimeColumn.setCellValueFactory(cellData -> {
           
            Appointment appt = cellData.getValue();
           
            LocalDateTime dt = appt.getDateTime(); 
            String formattedDateTime = (dt != null) ? dt.toString() : "N/A"; 
         
            return new SimpleStringProperty(formattedDateTime);
        });
      
        specialistColumn.setCellValueFactory(cellData -> {
             Appointment appt = cellData.getValue();
            
             Specialist s = appt.getSpecialist(); 
             return new SimpleStringProperty(s != null ? s.getName() : "Unknown Specialist");
        });
      

        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));

        // Bind the ObservableList to the TableView
        appointmentsTableView.setItems(appointmentData);

        // Initially disable modify/cancel buttons until an appointment is selected
        modifyButton.setDisable(true);
        cancelButton.setDisable(true);

        // Add a listener to enable/disable buttons based on selection
        appointmentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isItemSelected = (newSelection != null);
            modifyButton.setDisable(!isItemSelected);
            cancelButton.setDisable(!isItemSelected);
        });

        // Optional: Load all appointments initially
        loadAllAppointments();
    }

    
    @FXML
    private void handleSearchPatient(ActionEvent event) {
        String patientId = patientIdSearchField.getText().trim();

        if (patientId.isEmpty()) {
            // If search field is empty, show all appointments
            loadAllAppointments();
            return;
        }

       
        ObservableList<Appointment> filteredAppointments = DataStore.appointments.stream()
                .filter(appointment -> {
                    Patient patient = appointment.getPatient(); // Requires getPatient() in Appointment
                    
                    return patient != null && patientId.equals(patient.getPatientId()); // Error-free after adding getPatientId() to Patient
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // Update the TableView with filtered data
        appointmentData.setAll(filteredAppointments);

        if (filteredAppointments.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Results", "No appointments found for patient ID: " + patientId);
        }
    }

    /**
     * Loads all appointments into the TableView.
     * This can be called on initialization or refresh.
     */
    private void loadAllAppointments() {
        appointmentData.setAll(DataStore.appointments);
    }

    /**
     * Handles the refresh button action.
     * Reloads all appointments from the DataStore.
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        loadAllAppointments();
        patientIdSearchField.clear(); // Clear search field on refresh
    }

   
    @FXML
    private void handleModifyAppointment(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an appointment to modify.");
            return;
        }

        // Check if appointment is in a modifiable state
        AppointmentStatus status = selectedAppointment.getStatus();
        if (status == AppointmentStatus.CANCELLED || status == AppointmentStatus.COMPLETED) {
             showAlert(Alert.AlertType.WARNING, "Invalid Action", "Cannot modify an appointment with status: " + status);
             return;
        }

       
        Dialog<LocalDateTime> modifyDialog = new Dialog<>();
        modifyDialog.setTitle("Modify Appointment");
        modifyDialog.setHeaderText("Modify Appointment: " + selectedAppointment.getId() + "\nEnter new date and time (YYYY-MM-DDTHH:MM):");

        ButtonType modifyButtonType = new ButtonType("Modify", ButtonBar.ButtonData.OK_DONE);
        modifyDialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        TextField dateTimeField = new TextField();
       
        dateTimeField.setPromptText("e.g., 2025-10-15T14:30");

        TextArea reasonArea = new TextArea();
        reasonArea.setPromptText("Reason for modification");
        reasonArea.setPrefRowCount(3);

        modifyDialog.getDialogPane().setContent(new javafx.scene.layout.VBox(10,
                new Label("New Date & Time (ISO format):"), dateTimeField,
                new Label("Reason:"), reasonArea));

        modifyDialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                try {
                    String dateTimeStr = dateTimeField.getText().trim();
                    if (!dateTimeStr.isEmpty()) {
                        // Parsing ISO format LocalDateTime string (e.g., "2025-10-15T14:30")
                        return LocalDateTime.parse(dateTimeStr);
                    }
                } catch (Exception e) {
                    // Parsing failed, dialog will close without result
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid date/time format. Please use ISO format (e.g., 2025-10-15T14:30).");
                }
            }
            return null; // Cancel or invalid input
        });

        modifyDialog.showAndWait().ifPresent(newDateTime -> {
            String reason = reasonArea.getText().trim();
            if (newDateTime != null) {
                boolean success = currentStaff.modifyAppointment(selectedAppointment, newDateTime, reason);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment modified successfully.");
                    handleRefresh(new ActionEvent()); // Refresh the table view
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to modify appointment.");
                }
            } 
        });
       
    }


   
    @FXML
    private void handleCancelAppointment(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an appointment to cancel.");
            return;
        }

        // Check if appointment is cancellable
        AppointmentStatus status = selectedAppointment.getStatus();
        if (status == AppointmentStatus.CANCELLED || status == AppointmentStatus.COMPLETED) {
             showAlert(Alert.AlertType.WARNING, "Invalid Action", "Cannot cancel an appointment with status: " + status);
             return;
        }

        // Create a simple dialog to get the cancellation reason
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Cancel Appointment");
        dialog.setHeaderText("Cancel Appointment: " + selectedAppointment.getId() + "\nPlease provide a reason:");

        ButtonType confirmButtonType = new ButtonType("Confirm Cancel", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        TextField reasonField = new TextField();
        reasonField.setPromptText("Reason for cancellation");
        dialog.getDialogPane().setContent(new javafx.scene.layout.VBox(10, new Label("Reason:"), reasonField));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return reasonField.getText().trim();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(reason -> {
            if (reason != null && !reason.isEmpty()) { // Check for null and empty reason
                // Call the HealthcareStaff model method (delegates to Appointment)
                boolean success = currentStaff.cancelAppointment(selectedAppointment, reason);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment cancelled successfully.");
                    handleRefresh(new ActionEvent()); // Refresh the table view
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to cancel appointment.");
                }
            } else {
                 showAlert(Alert.AlertType.WARNING, "Invalid Input", "Cancellation reason cannot be empty.");
            }
        });
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void openStaffDashboard(MouseEvent event) {
        handleRefresh(new ActionEvent()); 
    }

    // Generic navigation helper (example)
    private void navigateTo(String fxmlPath, String title, ActionEvent event) { // Changed event type
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
             Scene scene = new Scene(loader.load());
             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Requires casting event.getSource() correctly
             stage.setScene(scene);
             stage.setTitle(title);
             stage.show();
         } catch (IOException e) {
             e.printStackTrace();
             showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load the " + title + " view.");
         } catch (ClassCastException e) {
             // Handle case where event.getSource() is not a Node
             e.printStackTrace();
             showAlert(Alert.AlertType.ERROR, "Navigation Error", "Invalid source for navigation event.");
         }
     }


}