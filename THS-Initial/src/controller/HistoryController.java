package controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.HistoryRow;
import service.AppointmentHistoryService;
import util.ServiceLocator;
import util.Session;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryController {

    @FXML private TableView<HistoryRow> appointmentTable;
    @FXML private TableColumn<HistoryRow, String> patientCol;
    @FXML private TableColumn<HistoryRow, String> specialistCol;
    @FXML private TableColumn<HistoryRow, String> dateCol;
    @FXML private TableColumn<HistoryRow, String> reasonCol;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final AppointmentHistoryService historyService = ServiceLocator.appointmentHistory();

    @FXML
    public void initialize() {
        patientCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getPatientName()));
        specialistCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getSpecialistName()));
        dateCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getDateTime() == null ? "" : c.getValue().getDateTime().format(fmt)));
        reasonCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getReason()));

        String patientId = (Session.currentPatientId != null && !Session.currentPatientId.isBlank())
                ? Session.currentPatientId : "P001";

        List<HistoryRow> rows = historyService.historyForPatient(patientId).stream()
                .map(r -> new HistoryRow(r.patientName(), r.specialistName(), r.dateTime(), r.reason()))
                .toList();

        appointmentTable.getItems().setAll(rows);
    }
}
