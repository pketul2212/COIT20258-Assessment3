
package com.ths.enhanced.client.controller;

import com.ths.enhanced.client.app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PatientController {
    @FXML 
    private TextField patientIdField, specialistIdField, startAtField, notesField;
    
    @FXML 
    private Button bookBtn, listRemindersBtn;
    
    @FXML 
    private TextArea output;

    @FXML 
    public void onBook() {
        try {
            var resp = MainApp.NET.send("BOOK_APPT", Map.of(
                    "patientId", Integer.parseInt(patientIdField.getText().trim()),
                    "specialistId", Integer.parseInt(specialistIdField.getText().trim()),
                    "startAt", startAtField.getText().trim(), // e.g., 2025-10-21T10:30
                    "notes", notesField.getText().trim()
            ));
            output.setText(resp.toString());
        } catch (Exception e) { output.setText(e.getMessage()); }
    }

    @FXML 
    public void onListReminders() {
        try {
            var resp = MainApp.NET.send("LIST_REMINDERS", Map.of(
                    "patientId", Integer.parseInt(patientIdField.getText().trim())
            ));
            output.setText(resp.toString());
        } catch (Exception e) { output.setText(e.getMessage()); }
    }
}
