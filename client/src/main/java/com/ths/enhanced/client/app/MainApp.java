
package com.ths.enhanced.client.app;

import com.ths.enhanced.client.net.NetClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static NetClient NET;

    @Override 
    public void start(Stage stage) throws Exception {
        NET = new NetClient("localhost", 5050);
        NET.connect();
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        stage.setScene(new Scene(fxml.load(), 480, 360));
        stage.setTitle("THS-Enhanced Client");
        stage.show();
    }

    @Override 
    public void stop() throws Exception {
        try {
            if (NET != null) 
                NET.close(); 
        } catch (Exception ignored){}
    }

    public static void main(String[] args) { 
        launch(args); 
    }
}
