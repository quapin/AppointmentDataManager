package com.healthcare.controllers;

import com.healthcare.utils.AuthenticationService;
import com.healthcare.DatabaseManager;
import com.healthcare.utils.HashingUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private AuthenticationService authService;

    @FXML
    public void initialize(){
        try{
            DatabaseManager dbManager = new DatabaseManager();
            authService = new AuthenticationService(dbManager);
        } catch(SQLException e){
            showError("Database Error", "An error occurred while connecting to the database");
        }
    }

    @FXML
    public void handleLoginAction(){
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()){
            showError("Validation Error", "Please enter both username and password");
            return;
        }

        try{
            boolean isAuthenticated = authService.authenticateUser(username, HashingUtil.hashPassword(password));
            if(isAuthenticated){
                //showInfo("Login Successful", "Welcome " + username + "!");

                navigateToDashboard(username, authService.getUserRole(username));
            } else{
                showError("Login Failed", "Invalid username or password");
            }
        } catch(SQLException e){
            showError("Error", "Unexpected error occured. Try again later.");
        }
    }

    private void showError(String title, String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(error);
        alert.showAndWait();
    }

    private void showInfo(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToDashboard(String username, String role){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/healthcare/views/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUser(username, role);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Healthcare Dashboard");
            stage.show();
        }catch (IOException e){
            showError("Error", "An error occurred while navigating to dashboard");
            e.printStackTrace();
        }
    }
}
