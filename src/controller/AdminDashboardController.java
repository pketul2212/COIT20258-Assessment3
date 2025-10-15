package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.AdminAnalyticsService;
import util.ServiceLocator;
import util.Session;

public class AdminDashboardController {

    @FXML private Label totalConsultationsLabel;
    @FXML private Label totalPatientsLabel;
    @FXML private Label totalSpecialistsLabel;
    @FXML private BarChart<String, Number> consultationChart;

    private final AdminAnalyticsService analytics = ServiceLocator.adminAnalytics();

    @FXML
    public void initialize() {
        totalConsultationsLabel.setText(String.valueOf(analytics.totalConsultations()));
        totalPatientsLabel.setText(String.valueOf(analytics.totalPatients()));
        totalSpecialistsLabel.setText(String.valueOf(analytics.totalSpecialists()));

        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Consultations");
        s.getData().add(new XYChart.Data<>("Today", analytics.consultationsToday()));
        s.getData().add(new XYChart.Data<>("This Week", analytics.consultationsThisWeek()));
        s.getData().add(new XYChart.Data<>("This Month", analytics.consultationsThisMonth()));

        consultationChart.getData().clear();
        consultationChart.getData().add(s);
    }

    @FXML
    private void handleLogout() {
        try {
            Session.clear();
            // get current stage via any node on the scene graph
            Stage stage = (Stage) consultationChart.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            stage.setTitle("Telehealth System - Login");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
private void openAddDoctor() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_doctor.fxml"));
        Parent root = loader.load();  // 👈 cast to Parent (this line fixes your error)
        Stage stage = new Stage();
        stage.setTitle("Add Doctor");
        stage.setScene(new Scene(root));
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
        new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                "Failed to open Add Doctor screen: " + e.getMessage()).showAndWait();
    }
}


}
