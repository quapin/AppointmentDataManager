package com.healthcare.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOError;
import java.io.IOException;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button managePatientsButton;

    @FXML
    private Button viewAppointmentsButton;

    @FXML
    private Button manageRecordsButton;

    @FXML
    private Button analyticsButton;

    private String username;
    private String role;

    @FXML
    public void setUser(String username, String role) {
        this.username = username;
        this.role = role;
        welcomeLabel.setText("Welcome " + username + "!");

        // Role based buttons
        if ("provider".equals(role)) {
            managePatientsButton.setDisable(false);
            viewAppointmentsButton.setCancelButton(false);
            manageRecordsButton.setDisable(false);
            analyticsButton.setDisable(false);
        } else if ("patient".equals(role)) {
            managePatientsButton.setVisible(false);
            viewAppointmentsButton.setVisible(false);
            analyticsButton.setVisible(false);
            viewAppointmentsButton.setDisable(false);
        }
    }

    @FXML
    private void handleManagePatients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/healthcare/views/patient.fxml"));
            Parent root = loader.load();

            // Pass the current stage or scene to the next controller
            PatientController controller = loader.getController();
            controller.setPreviousScene(managePatientsButton.getScene());

            Stage stage = (Stage) managePatientsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Patients");
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load Manage Patients screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewAppointments() {
        System.out.println("Navigate to appointments screen.");
        // Todo: Navigation to appointments.fxml
    }

    @FXML
    private void handleManageRecords() {
        System.out.println("Navigate to records screen.");
        // Todo: Navigation to record.fxml
    }

    @FXML
    private void handleViewAnalytics() {
        System.out.println("Navigate to analytics screen.");
        // Todo: Navigation to analytics.fxml
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/healthcare/views/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load login screen: " + e.getMessage());
            e.printStackTrace();
        }


    }

}
