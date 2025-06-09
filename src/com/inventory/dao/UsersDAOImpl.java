package com.inventory.dao;

import com.inventory.model.User;
import com.inventory.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDAOImpl implements UsersDAO {
    @Override
    public User getUserById(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password")); // NEVER USE THIS IN REAL APPLICATION.
                user.setRoleID(rs.getInt("RoleID"));
                return user;
            }
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
         try (Connection conn = DatabaseUtil.getConnection();
              PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Users WHERE Username = ?")) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));  // NEVER USE THIS IN REAL APPLICATION.
                user.setRoleID(rs.getInt("RoleID"));
                return user;
            }
            return null;
        }
    }
    
    @Override
    public int getUserIdByUsername(String username) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT UserID FROM Users WHERE Username = ?")) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("UserID");
            }
            return -1;
        } 
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Users")) {
            ResultSet rs = pstmt.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));  // NEVER USE THIS IN REAL APPLICATION.
                user.setRoleID(rs.getInt("RoleID"));
                users.add(user);
            }
            return users;
        }
    }

    @Override
    public void addUser(User user) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO Users (Username, Password, RoleID) VALUES (?, ?, ?)")) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Ideally, use hashed password
            pstmt.setInt(3, user.getRoleID());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateUser(User user) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE Users SET Username = ?, Password = ?, RoleID = ? WHERE UserID = ?")) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());  // Ideally, use hashed password
            pstmt.setInt(3, user.getRoleID());
            pstmt.setInt(4, user.getUserID());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Users WHERE UserID = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    
    @Override
    public List<User> searchUsers(String searchTerm) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Users WHERE Username LIKE ?")) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setRoleID(rs.getInt("RoleID"));
            }
            return users;
        }
    }
}