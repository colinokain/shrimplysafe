/*
 * The MIT License
 *
 * Copyright 2022 colin.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.uah.hackathon2022.Database;

import org.json.JSONException;
import org.json.JSONObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.DriverManager;
import java.sql.Connection;

/**
 *
 * @authors Colin O'Kain, Joshua Payne
 */
public class DatabaseConnection
{
    Connection connection;
    public static DatabaseConnection instance;
    
    private DatabaseConnection() throws SQLException {
        this.connection = null;
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hackathon", "root", "password");
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (this.connection != null) {
            System.out.println("Connection initialized");
        }
    }
    
    public static DatabaseConnection getInstance() throws SQLException {
        if (DatabaseConnection.instance == null) {
            DatabaseConnection.instance = new DatabaseConnection();
        }
        return DatabaseConnection.instance;
    }
    
    public boolean addUser(final String username) {
        if (DatabaseConnection.instance == null) {
            return false;
        }
        if (username.matches("[^a-zA-Z0-9 -]")) {
            System.out.println("INSIDE");
            return false;
        }
        try {
            final ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM usernames ORDER BY ID DESC LIMIT 1");
            int ct = 1;
            while (rs.next()) {
                ct = rs.getInt(1) + 1;
            }
            final String query = "INSERT INTO usernames (value, id)\nVALUES ('" + username + "', " + ct + ")";
            final PreparedStatement ps = this.connection.prepareStatement(query);
            ps.execute();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return true;
    }
    
    public boolean addPassword(final String hashedPassword) {
        try {
            final ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM passwords ORDER BY ID DESC LIMIT 1");
            int ct = 1;
            while (rs.next()) {
                ct = rs.getInt(1) + 1;
            }
            final String query = "INSERT INTO passwords (value, id)\nVALUES ('" + hashedPassword + "', " + ct + ")";
            final PreparedStatement ps = this.connection.prepareStatement(query);
            ps.execute();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return true;
    }
    
    public JSONObject getUser(final String username, final String password) throws SQLException, JSONException {
        final JSONObject returnValue = new JSONObject();
        ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM usernames WHERE value=\"" + username + "\"");
        if (!rs.next()) {
            returnValue.put("result", (Object)"failure");
            return returnValue;
        }
        rs = this.connection.createStatement().executeQuery("SELECT * FROM passwords WHERE value=\"" + password + "\"");
        if (!rs.next()) {
            returnValue.put("result", (Object)"failure");
            return returnValue;
        }
        returnValue.put("result", (Object)"success");
        returnValue.put("id", rs.getInt(1));
        return returnValue;
    }
    
    public JSONObject updatePassword(final String username, final String oldPassword, final String newPassword) throws SQLException, JSONException {
        final JSONObject returnValue = new JSONObject();
        final JSONObject user = this.getUser(username, oldPassword);
        if (user.getString("result").equals("failure")) {
            return user;
        }
        final int id = user.getInt("id");
        final ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM passwords WHERE id=\"" + id + "\"");
        if (!rs.next()) {
            returnValue.put("result", (Object)"failure");
            return returnValue;
        }
        final String query = "UPDATE passwords SET value=\"" + newPassword + "\" WHERE id=" + id;
        final PreparedStatement ps = this.connection.prepareStatement(query);
        ps.execute();
        returnValue.put("result", (Object)"success");
        return returnValue;
    }
    
    static {
        DatabaseConnection.instance = null;
    }
}