package com.healthcare.utils;

import com.healthcare.DatabaseManager;
import java.sql.SQLException;
import java.sql.ResultSet;

public class PatientService {
    private final DatabaseManager dbManager;

    public PatientService(DatabaseManager dbManager){
        this.dbManager = dbManager;
    }

    public void addPatient(String name, int age, String contact) throws SQLException{
        dbManager.addPatient(name, age, contact);
    }

    public void updatePatient(int patientID, String name, int age, String contact) throws SQLException{
        dbManager.updatePatient(patientID, name, age, contact);
    }

    public void deletePatient(int patientID) throws SQLException{
        dbManager.deletePatient(patientID);
    }

    public ResultSet fetchAllPatients() throws SQLException{
        return dbManager.fetchAllPatients();
    }

}
