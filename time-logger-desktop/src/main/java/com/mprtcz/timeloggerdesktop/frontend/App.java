package com.mprtcz.timeloggerdesktop.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class App extends Application {

    public void start(Stage window) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/mainMenu.fxml"));
        System.out.println(getClass().getResource("/"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 500, 800);

        scene.getStylesheets().add(App.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
        scene.getStylesheets().add(App.class.getResource("/css/my-styles.css").toExternalForm());


        window.setTitle("Time Logger");
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}