
package com.ths.enhanced.client.controller;

import com.ths.enhanced.client.app.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Map;

public class LoginController {
    @FXML 
    private TextField username;
    
    @FXML 
    private PasswordField password;
    
    @FXML 
    private Button loginBtn;

    @FXML 
    public void onLogin() {
        try {
            Map resp = MainApp.NET.send("LOGIN", Map.of("username", username.getText().trim(), "password", password.getText()));
            if (!(Boolean) resp.get("ok")) {
                username.setText("Invalid"); 
                return; 
            }
            
            String role = (String) resp.get("role");
            int userId = ((Double) resp.get("userId")).intValue();
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            
            if ("PATIENT".equals(role)) {
                FXMLLoader f = new FXMLLoader(getClass().getResource("/view/patient.fxml"));
                stage.setScene(new Scene(f.load(), 700, 500));
            } else if ("SPECIALIST".equals(role)) {
                FXMLLoader f = new FXMLLoader(getClass().getResource("/view/specialist.fxml"));
                stage.setScene(new Scene(f.load(), 800, 500));
            } else {
                FXMLLoader f = new FXMLLoader(getClass().getResource("/view/staff.fxml"));
                stage.setScene(new Scene(f.load(), 600, 400));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
