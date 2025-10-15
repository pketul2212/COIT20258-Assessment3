// src/util/Navigator.java
package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class Navigator {
    private Navigator() {}

    public static void openWindow(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(Navigator.class.getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.NONE);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // later: use Logger + user-friendly Alert
        }
    }
}
