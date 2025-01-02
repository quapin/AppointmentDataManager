package com.healthcare;
import java.sql.*;
import com.healthcare.utils.HashingUtil;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\etho1\\IdeaProjects\\AppointmentDataManager\\src\\com\\healthcare\\resources\\healthcare.db";

    private Connection connection;

    public DatabaseManager() throws SQLException{
        // Database Connection
        connect();
        initializeDatabase();
    }

    public Connection getConnection(){
        if (connection == null){
            throw new IllegalStateException("Database connection is not yet established.");
        }
        return connection;
    }

    private void connect() throws SQLException{
        connection = DriverManager.getConnection(DB_URL);
        System.out.println("Connection to SQLite established.");
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection to SQLite closed.");
            } catch (SQLException e) {
                System.err.println("Failed to close connection: " + e.getMessage());
            } finally {
                connection = null; // Prevent memory leaks by nullifying the connection reference
            }
        } else {
            System.out.println("No active connection to close.");
        }
    }

    private void initializeDatabase() throws SQLException{
            // Creating users table
            String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL);
                    """;
            // Creating patients table
            String createPatientsTable = """
                    CREATE TABLE IF NOT EXISTS patients (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    age INTEGER,
                    contact TEXT
                    );
                    """;
            // Creating health_records table
            String createAppointmentsTable = """
                    CREATE TABLE IF NOT EXISTS appointments (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    patient_id INTEGER NOT NULL,
                    date_time TEXT NOT NULL,
                    type TEXT,
                    notes TEXT
                    );
                    """;
            String createHealthRecordsTable = """
                    CREATE TABLE IF NOT EXISTS health_records (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    patient_id INTEGER NOT NULL,
                    record_type TEXT NOT NULL,
                    details TEXT,
                    FOREIGN KEY(patient_id) REFERENCES patients(id)
                    );
                    """;

            try (Statement stmt = connection.createStatement()){
                stmt.execute(createUsersTable);
                stmt.execute(createPatientsTable);
                stmt.execute(createAppointmentsTable);
                stmt.execute(createHealthRecordsTable);
                System.out.println("Tables Initialized Successfully");
            } catch(SQLException e){
                System.err.println("Failed to initialize tables: " + e.getMessage());
            }
    }

    public void addUser(String username, String password, String role) throws SQLException{
        String hashedPassword = HashingUtil.hashPassword(password);
        String sql = """
                INSERT INTO users (username, password, role) VALUES (?, ?, ?);
                """;
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            System.out.println("User added successfully.");
        } catch(SQLException e){
            System.err.println("Failed to add user: " + e.getMessage());
        }
    }

    public void addPatient(String name, int age, String contact) throws SQLException{
        String sql = """
                INSERT INTO patients (name, age, contact) VALUES (?, ?, ?);
                """;
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, contact);
            pstmt.executeUpdate();
            System.out.println("Patient added successfully.");
        } catch(SQLException e){
            System.err.println("Failed to add patient: " + e.getMessage());
        }
    }

    public void addAppointment(int patientId, String date_time, String type, String notes)throws SQLException{
        String sql = """
                INSERT INTO appointments (patient_id, date_time, type, notes) VALUES
                (?, ?, ?, ?);
                """;
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, patientId);
            pstmt.setString(2, date_time);
            pstmt.setString(3, type);
            pstmt.setString(4, notes);
            pstmt.executeUpdate();
            System.out.println("Appointment added successfully.");
        } catch(SQLException e){
            System.err.println("Failed to add appointment: " + e.getMessage());
        }
    }

    public void addHealthRecord(int patientId, String recordType, String details) throws SQLException{
        String sql = """
                INSERT INTO health_records (patient_id, record_type, details) VALUES (?, ?, ?);
                """;
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, patientId);
            pstmt.setString(2, recordType);
            pstmt.setString(3, details);
            pstmt.executeUpdate();
            System.out.println("Health Record added successfully.");
        } catch(SQLException e){
            System.err.println("Failed to add health record: " + e.getMessage());
        }
    }

    public ResultSet fetchAllUsers() throws SQLException{
        String sql = "SELECT * FROM users";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public ResultSet fetchAllPatients() throws SQLException{
        String sql = "SELECT * FROM patients";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public ResultSet fetchAllAppointments() throws SQLException{
        String sql = "SELECT * FROM appointments";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public ResultSet fetchAllHealthRecords() throws SQLException{
        String sql = "SELECT * FROM health_records";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public void deleteUser(int userId) throws SQLException{
        String sql = "DELETE FROM users WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("User deleted successfully.");
        } catch(SQLException e){
            System.err.println("Failed to delete user: " + e.getMessage());
        }
    }

    public void deletePatient(int patientId) throws SQLException{
        String sql = "DELETE FROM patients WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, patientId);
            pstmt.executeUpdate();
            System.out.println("Patient deleted successfully.");
        } catch(SQLException e){
            System.err.println("Failed to delete patient: " + e.getMessage());
        }
    }

    public void deleteAppointment(int appointmentId) throws SQLException{
        String sql = "DELETE FROM appointments WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, appointmentId);
            pstmt.executeUpdate();
            System.out.println("Appointment cancelled successfully.");
        } catch(SQLException e){
            System.err.println("Failed to cancel appointment with ID: " + appointmentId + e.getMessage());
        }
    }

    public void updateUser(int userId, String username, String password, String role) throws SQLException{
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.setString(2, HashingUtil.hashPassword(password));
            pstmt.setString(3, role);
            pstmt.setInt(4, userId);
            pstmt.executeUpdate();
            System.out.println("User updated successfully.");
        } catch(SQLException e){
            System.err.println("Failed to update user: " + e.getMessage());
        }
    }

    public void updatePatient(int patientId, String name, int age, String contact) throws SQLException{
        String sql = "UPDATE patients SET name = ?, age = ?, contact = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, contact);
            pstmt.setInt(4, patientId);
            pstmt.executeUpdate();
            System.out.println("Patient updated successfully.");
        } catch(SQLException e){
            System.err.println("Failed to update patient: " + e.getMessage());
        }
    }

    public void updateAppointment(int appointmentId, int patientId, String date_time, String type, String notes) throws SQLException{
        String sql = "UPDATE appointments SET patient_id = ?, date_time = ?, type = ?, notes = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, patientId);
            pstmt.setString(2, date_time);
            pstmt.setString(3, type);
            pstmt.setString(4, notes);
            pstmt.setInt(5, appointmentId);
            pstmt.executeUpdate();
            System.out.println("Appointment updated successfully.");
        } catch(SQLException e){
            System.err.println("Failed to update appointment: " + e.getMessage());
        }
    }

    public void updateHealthRecord(int recordId, int patientId, String recordType, String details) throws SQLException{
        String sql = "UPDATE health_records SET patient_id = ?, record_type = ?, details = ? WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, patientId);
            pstmt.setString(2, recordType);
            pstmt.setString(3, details);
            pstmt.setInt(4, recordId);
            pstmt.executeUpdate();
            System.out.println("Health Record updated successfully.");
        } catch(SQLException e){
            System.err.println("Failed to update health record: " + e.getMessage());
        }
    }

    public ResultSet fetchUserByUsername(String username) throws SQLException{
        String sql = "SELECT * FROM users WHERE username = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            return pstmt.executeQuery();
        } catch(SQLException e){
            System.err.println("Failed to fetch user: " + e.getMessage());
            return null;
        }
    }

    public ResultSet fetchAppointmentsByPatientId(int patientId) throws SQLException{
        String sql = "SELECT * FROM appointments WHERE patient_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, patientId);
            return pstmt.executeQuery();
        } catch(SQLException e){
            System.err.println("Failed to fetch appointments: " + e.getMessage());
            return null;
        }
    }
}
