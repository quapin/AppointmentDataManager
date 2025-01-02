package com.healthcare.utils;

import com.healthcare.DatabaseManager;
import java.sql.SQLException;
import java.sql.ResultSet;

public class AppointmentService {
    private final DatabaseManager dbManager;

    public AppointmentService(DatabaseManager dbManager){
        this.dbManager = dbManager;
    }

    public void addAppointment(int patientID, String date_time, String type, String notes) throws SQLException{
        dbManager.addAppointment(patientID, date_time, type, notes);
    }

    public void deleteAppointment(int appointmentID) throws SQLException{
        dbManager.deleteAppointment(appointmentID);
    }

    public ResultSet fetchAppointmentsByPatient(int patientID) throws SQLException{
        return dbManager.fetchAppointmentsByPatientId(patientID);
    }

    public ResultSet fetchUpcomingAppointments(String date) throws SQLException{
        String sql = "SELECT * FROM appointments WHERE date_time >= ?";
        try (var pstmt = dbManager.getConnection().prepareStatement(sql)){
            pstmt.setString(1, date);
            return pstmt.executeQuery();
        }
    }
}
