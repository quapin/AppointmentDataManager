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
        System.out.println("Provided Hashed Password: " + hashedPassword);

        ResultSet rs = dbManager.fetchUserByUsername(username);

        if (rs != null && rs.next()) {
            String storedPassword = rs.getString("password");
            System.out.println("Stored Password Hash: " + storedPassword);

            boolean match = storedPassword.equals(hashedPassword);
            System.out.println("Password Match: " + match);
            return match;
        }

        System.out.println("No user found for username: " + username);
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