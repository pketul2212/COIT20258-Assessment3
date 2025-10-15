package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.RegistrationService;
import util.ServiceLocator;

import java.util.regex.Pattern;

public class AddDoctorController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField specialtyField;
    @FXML private TextField phoneField;

    private final RegistrationService registration = ServiceLocator.registration();
    private static final Pattern EMAIL_RX = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @FXML
    private void handleCreate() {
        String name  = text(fullNameField);
        String email = text(emailField);
        String pass  = text(passwordField);
        String spec  = text(specialtyField);
        String phone = text(phoneField);

        if (name.isBlank() || email.isBlank() || pass.isBlank() || spec.isBlank()) {
            error("Please fill in Full Name, Email, Password and Specialty.");
            return;
        }
        if (!EMAIL_RX.matcher(email).matches()) {
            error("Please enter a valid email address.");
            return;
        }
        if (pass.length() < 6) {
            error("Password should be at least 6 characters.");
            return;
        }

        try {
            String specialistId = registration.registerSpecialist(name, email, pass, spec, phone);
            info("Doctor created: " + name + " (ID: " + specialistId + ")");
            close();
        } catch (IllegalStateException dup) {
            error("Email is already registered.");
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Failed to create doctor: " + ex.getMessage());
        }
    }

    @FXML private void handleClose() { close(); }

    private void close() {
        Stage s = (Stage) fullNameField.getScene().getWindow();
        s.close();
    }
    private String text(TextInputControl c) { return c.getText()==null? "": c.getText().trim(); }
    private void error(String m){ new Alert(Alert.AlertType.ERROR, m).showAndWait(); }
    private void info(String m){ new Alert(Alert.AlertType.INFORMATION, m).showAndWait(); }
}
