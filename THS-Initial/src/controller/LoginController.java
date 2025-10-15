package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.AuthService;
import service.RegistrationService;
import util.ServiceLocator;
import util.Session;

import java.util.regex.Pattern;

public class LoginController {

    // ---- Login fx:ids ----
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    // ---- Register fx:ids ----
    @FXML private TextField fullNameField;
    @FXML private TextField emailRegField;
    @FXML private PasswordField passwordRegField;
    @FXML private PasswordField confirmPasswordRegField;

    private static final Pattern EMAIL_RX = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private final AuthService auth = ServiceLocator.auth();
    private final RegistrationService registration = ServiceLocator.registration();

    @FXML
    private void initialize() {
        // Ensure a default admin exists (admin@ths.com / admin123)
        auth.ensureDefaultAdmin("admin@ths.com", "admin123");
    }

    // ===== LOGIN =====
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = safeText(usernameField);
        String pass  = safeText(passwordField);

        if (email.isBlank() || pass.isBlank()) { error("Please enter both email and password."); return; }
        if (!EMAIL_RX.matcher(email).matches()) { error("Please enter a valid email address."); return; }

        if (!auth.login(email, pass)) { error("Invalid credentials."); return; }

        // route by role
        switch (Session.currentRole) {
            case "ADMIN" -> swapScene("/view/admin_dashboard.fxml", "Admin Dashboard");
            case "PATIENT" -> swapScene("/view/history.fxml", "Appointment & Prescription History");
            case "SPECIALIST" -> swapScene("/view/specialist_dashboard.fxml", "Specialist Dashboard");
            default -> error("Unknown role: " + Session.currentRole);
        }
    }

    // ===== SIGN UP =====
    @FXML
    private void handleSignUp(ActionEvent event) {
        String name  = safeText(fullNameField);
        String email = safeText(emailRegField);
        String pass1 = safeText(passwordRegField);
        String pass2 = safeText(confirmPasswordRegField);

        if (name.isBlank() || email.isBlank() || pass1.isBlank() || pass2.isBlank()) { error("Please fill in all fields."); return; }
        if (!EMAIL_RX.matcher(email).matches()) { error("Please enter a valid email address."); return; }
        if (!pass1.equals(pass2)) { error("Passwords do not match."); return; }
        if (pass1.length() < 6) { error("Password should be at least 6 characters."); return; }

        try {
            String patientId = registration.registerPatient(name, email, pass1);
            info("Registration successful for " + name + " (ID: " + patientId + "). You can now log in.");
            clearRegisterForm();
        } catch (IllegalStateException dup) {
            error("Email already registered. Try logging in.");
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Registration failed: " + ex.getMessage());
        }
    }

    // ===== Helpers =====

    private void swapScene(String fxmlPath, String title) {
    try {
        var url = getClass().getResource(fxmlPath);
        if (url == null) { error("FXML not found: " + fxmlPath); return; }
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) (loginButton != null
                ? loginButton.getScene().getWindow()
                : usernameField.getScene().getWindow());
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
        Throwable cause = e.getCause();
        String msg = e.getClass().getSimpleName() + (e.getMessage()!=null?": "+e.getMessage():"");
        while (cause != null) { msg += "\nCaused by: " + cause.getClass().getSimpleName() +
                                       (cause.getMessage()!=null?": "+cause.getMessage():""); cause = cause.getCause(); }
        error("Failed to open screen (" + fxmlPath + "):\n" + msg);
    }
}


    private void error(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null); a.setTitle("Error"); a.setContentText(msg); a.showAndWait();
    }
    private void info(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null); a.setTitle("Success"); a.setContentText(msg); a.showAndWait();
    }
    private String safeText(TextInputControl c) {
        return c == null || c.getText() == null ? "" : c.getText().trim();
    }
    private void clearRegisterForm() {
        if (fullNameField != null) fullNameField.clear();
        if (emailRegField != null) emailRegField.clear();
        if (passwordRegField != null) passwordRegField.clear();
        if (confirmPasswordRegField != null) confirmPasswordRegField.clear();
    }
}
