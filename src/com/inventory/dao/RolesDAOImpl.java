package com.inventory.dao;

import com.inventory.model.Role;
import com.inventory.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolesDAOImpl implements RolesDAO {
    @Override
    public Role getRoleById(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Roles WHERE RoleID = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getInt("RoleID"));
                role.setRoleName(rs.getString("RoleName"));
                return role;
            }
            return null;
        }
    }

    @Override
    public List<Role> getAllRoles() throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Roles")) {
            ResultSet rs = pstmt.executeQuery();
            List<Role> roles = new ArrayList<>();
            while (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getInt("RoleID"));
                role.setRoleName(rs.getString("RoleName"));
                roles.add(role);
            }
            return roles;
        }
    }

    @Override
    public void addRole(Role role) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO Roles (RoleName) VALUES (?)")) {
            pstmt.setString(1, role.getRoleName());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateRole(Role role) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE Roles SET RoleName = ? WHERE RoleID = ?")) {
            pstmt.setString(1, role.getRoleName());
            pstmt.setInt(2, role.getRoleID());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteRole(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Roles WHERE RoleID = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public String getRoleNameById(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT RoleName FROM Roles WHERE RoleID = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("RoleName");
            }
            return null;
        }
    }

    // New method to get Role ID from Role Name
    @Override
    public int getRoleIdByName(String roleName) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT RoleID FROM Roles WHERE RoleName = ?")) {
            pstmt.setString(1, roleName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("RoleID");
            }
            return -1; // Or throw an exception if the role name is not found
        }
    }

    //Method to get all Role Names.
    @Override
     public List<String> getAllRoleNames() throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT RoleName FROM Roles")) {
            ResultSet rs = pstmt.executeQuery();
            List<String> roleNames = new ArrayList<>();
            while (rs.next()) {
                roleNames.add(rs.getString("RoleName"));
            }
            return roleNames;
        }
    }
}
