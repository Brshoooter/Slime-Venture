package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Save {

    public void bazaDeDate() {
        createTable();
    }

    // Method to create the table if it doesn't already exist
    private static void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:program.db");
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Save " +
                    "(col1 INT NOT NULL)";

            stmt.execute(sql);
            System.out.println("Table created successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    // Method to insert data into the table
    public static void insertData(int col1) {
        Connection c = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:program.db");
            String sql = "INSERT INTO Save (col1) VALUES (?)";
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, col1);
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    // Method to get all data from the table
    public static List<String[]> getData() {
        List<String[]> data = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:program.db");
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Save");

            while (rs.next()) {
                int col1 = rs.getInt("col1");


                String[] row = new String[] {
                        String.valueOf(col1),

                };
                System.out.println("Retrieved row: " + Arrays.toString(row)); // Log retrieved row
                data.add(row);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return data;
    }
}