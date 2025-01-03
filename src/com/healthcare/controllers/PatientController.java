package com.healthcare.controllers;

import com.healthcare.DatabaseManager;
import com.healthcare.models.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOError;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientController {

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, Integer> idColumn;

    @FXML
    private TableColumn<Patient, String> nameColumn;

    @FXML
    private TableColumn<Patient, Integer> ageColumn;

    @FXML
    private TableColumn<Patient, String> contactColumn;

    @FXML
    private Button addPatientButton;

    @FXML
    private Button editPatientButton;

    @FXML
    private Button deletePatientButton;

    private ObservableList<Patient> patientList = FXCollections.observableArrayList();

    private DatabaseManager dbManager;
    private Scene previousScene;

    @FXML
    public void initialize(){
        // Create columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));

        try{
            dbManager = new DatabaseManager();
            loadPatients();
        } catch (SQLException e) {
            showError("Error loading patients", "Failed to connect to the database.");
        }
    }

    private void loadPatients(){
        patientTable.getItems().clear();

        try{
            ResultSet rs = dbManager.fetchAllPatients();
            while (rs.next()){
                Patient patient = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("contact")
                );
                patientTable.getItems().add(patient);
            }
        }catch (SQLException e){
            showError("Error", "Failed to fetch patients from the database.");
        }
    }
    @FXML
    private void handleAddPatient() {
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Add Patient");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField ageField = new TextField();
        ageField.setPromptText("Age");

        TextField contactField = new TextField();
        contactField.setPromptText("Contact Info");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1,0);
        grid.add(new Label("Age:"), 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(new Label("Contact Info:"), 0, 2);
        grid.add(contactField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton ->{
            if(dialogButton == ButtonType.OK){
                try{
                    String name = nameField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    String contactInfo = contactField.getText();

                    dbManager.addPatient(name, age, contactInfo);

                    loadPatients();
                } catch (NumberFormatException e){
                    showErrorDialog("Invalid Input", "Please enter a valid age.");
                } catch (SQLException e){
                    showErrorDialog("Error", "Failed to add the patient.");
                }
            }
            return null;
                }
        );
        dialog.showAndWait();
    }

    private void showErrorDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleEditPatient() {
        // Get selected patient.
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showError("No Selection", "Please select a patient to edit.");
            return;
        }
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Edit Patient");

        // Fill input fields with current patient details.
        TextField nameField = new TextField(selectedPatient.getName());
        TextField ageField = new TextField(String.valueOf(selectedPatient.getAge()));
        TextField contactField = new TextField(selectedPatient.getContact());

        // Create a grid with all input fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Age:"), 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(new Label("Contact Info:"), 0, 2);
        grid.add(contactField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK){
                try{
                    String name = nameField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    String contactInfo = contactField.getText();

                    DatabaseManager dbManager = DatabaseManager.getInstance();
                    dbManager.updatePatient(selectedPatient.getId(), name, age, contactInfo);

                    loadPatients();
                } catch (NumberFormatException e){
                    showErrorDialog("Invalid Input", "Please enter a valid age.");
                } catch (SQLException e){
                    showErrorDialog("Error", "Failed to update the patient.");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    @FXML
    private void handleDeletePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showError("No Selection", "Please select a patient to delete.");
            return;
        }

        // Confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Delete Patient");
        confirmationAlert.setContentText("Are you sure you want to delete " + selectedPatient.getName() + "?");

        ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);
        if (result == ButtonType.OK) {
            try {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                dbManager.deletePatient(selectedPatient.getId());
                patientTable.getItems().remove(selectedPatient);
                System.out.println("Patient deleted: " + selectedPatient.getName());
            } catch (SQLException e) {
                showError("Error", "Failed to delete the patient.");
            }
        }
    }

    @FXML
    public void handleBack() {
        if (previousScene != null) {
            Stage stage = (Stage) patientTable.getScene().getWindow(); // Get the current stage
            stage.setScene(previousScene); // Set the previous scene
            stage.setTitle("Dashboard"); // Update the title
        } else {
            showError("Navigation Error", "No previous scene available.");
        }
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
