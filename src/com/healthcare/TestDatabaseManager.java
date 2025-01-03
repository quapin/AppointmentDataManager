package com.healthcare;

import com.healthcare.utils.HashingUtil;

import java.sql.*;
;

public class TestDatabaseManager {
    public static void main(String[] args) {
        DatabaseManager dbManager = null;
        try {
            // Initialize DatabaseManager
            dbManager = new DatabaseManager();

            // Test adding a user
            dbManager.addUser("provider", HashingUtil.hashPassword("password"), "provider");

            // Test fetching all users
            ResultSet rs = dbManager.fetchAllUsers();
            while (rs.next()) {
                System.out.println("User ID: " + rs.getInt("id") + ", Username: " + rs.getString("username") + ", Role: " + rs.getString("role"));
            }

            // Test adding a patient
            dbManager.addPatient("sigma", 30, "647-290-8726");

            // Test fetching all patients
            ResultSet patients = dbManager.fetchAllPatients();
            while (patients.next()) {
                System.out.println("Patient ID: " + patients.getInt("id") + ", Name: " + patients.getString("name"));
            }

        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        } finally {
            if (dbManager != null) {
                dbManager.closeConnection();
            }
        }
    }
}
