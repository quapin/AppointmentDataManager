package com.healthcare.utils;

import com.healthcare.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationService{
    private final DatabaseManager dbManager;

    public AuthenticationService(DatabaseManager dbManager){
        this.dbManager = dbManager;
    }

    // Verify password matches hashed password.
    public boolean authenticateUser(String username, String password) throws SQLException{
        String hashedPassword = HashingUtil.hashPassword(password);
        ResultSet rs = dbManager.fetchUserByUsername(username);
        if (rs.next()){
            String storedPassword = rs.getString("password");
            return storedPassword.equals(hashedPassword);
        }
        return false;
    }

    public String getUserRole(String username) throws SQLException{
        ResultSet rs = dbManager.fetchUserByUsername(username);
        if (rs.next()){
            return rs.getString("role");
        }
        return null; // User not found
    }
}