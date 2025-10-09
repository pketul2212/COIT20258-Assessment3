
package com.ths.enhanced.client.controller;

import com.ths.enhanced.client.app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Map;

public class SpecialistController {
    @FXML 
    private TextField specialistIdField, startAtField, endAtField;
    
    @FXML 
    private Button addAvailBtn, listAvailBtn;
    
    @FXML 
    private TextArea output;

    @FXML 
    public void onAddAvailability() {
        try {
            var resp = MainApp.NET.send("ADD_AVAIL", Map.of(
                    "specialistId", Integer.parseInt(specialistIdField.getText().trim()),
                    "startAt", startAtField.getText().trim(),
                    "endAt", endAtField.getText().trim()
            ));
            output.setText(resp.toString());
        } catch (Exception e) { output.setText(e.getMessage()); }
    }

    @FXML 
    public void onListAvailability() {
        try {
            var resp = MainApp.NET.send("LIST_AVAIL", Map.of(
                    "specialistId", Integer.parseInt(specialistIdField.getText().trim())
            ));
            output.setText(resp.toString());
        } catch (Exception e) { output.setText(e.getMessage()); }
    }
}
