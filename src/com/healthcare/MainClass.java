package com.healthcare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClass extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/login.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Healthcare Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e){
            e.printStackTrace();;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
