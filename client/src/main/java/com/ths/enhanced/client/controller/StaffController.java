
package com.ths.enhanced.client.controller;

import com.ths.enhanced.client.app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Map;

public class StaffController {
    @FXML 
    private TextField appointmentIdField, newStartAtField;
    
    @FXML 
    private TextArea output;

    @FXML 
    public void onReschedule() {
        try {
            var resp = MainApp.NET.send("RESCHEDULE", Map.of(
                    "appointmentId", Integer.parseInt(appointmentIdField.getText().trim()),
                    "newStartAt", newStartAtField.getText().trim()
            ));
            output.setText(resp.toString());
        } catch (Exception e) { output.setText(e.getMessage()); }
    }

    @FXML 
    public void onCancel() {
        try {
            var resp = MainApp.NET.send("CANCEL", Map.of(
                    "appointmentId", Integer.parseInt(appointmentIdField.getText().trim())
            ));
            output.setText(resp.toString());
        } catch (Exception e) { output.setText(e.getMessage()); }
    }
}
