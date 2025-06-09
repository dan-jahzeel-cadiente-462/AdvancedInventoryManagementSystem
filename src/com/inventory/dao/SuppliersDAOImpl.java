package com.inventory.dao;

import com.inventory.model.Supplier;
import com.inventory.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SuppliersDAOImpl implements SuppliersDAO {

    @Override
    public Supplier getSupplierById(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Suppliers WHERE SupplierID = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierID(rs.getInt("SupplierID"));
                supplier.setName(rs.getString("Name"));
                supplier.setContactInfo(rs.getString("ContactInfo"));
                return supplier;
            }
            return null;
        }
    }

    @Override
    public List<Supplier> getAllSuppliers() throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Suppliers")) {
            ResultSet rs = pstmt.executeQuery();
            List<Supplier> suppliers = new ArrayList<>();
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierID(rs.getInt("SupplierID"));
                supplier.setName(rs.getString("Name"));
                supplier.setContactInfo(rs.getString("ContactInfo"));
                suppliers.add(supplier);
            }
            return suppliers;
        }
    }
    
    @Override
    public List<String> getAllSuppliersByName() throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT Name FROM Suppliers")) {
            ResultSet rs = pstmt.executeQuery();
            List<String> suppliers = new ArrayList<>();
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setName(rs.getString("Name"));
                suppliers.add(supplier.getName());
            }
            return suppliers;
        }
    }

    @Override
    public void addSupplier(Supplier supplier) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO Suppliers (Name, ContactInfo) VALUES (?, ?)")) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getContactInfo());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateSupplier(Supplier supplier) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE Suppliers SET Name = ?, ContactInfo = ? WHERE SupplierID = ?")) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getContactInfo());
            pstmt.setInt(3, supplier.getSupplierID());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteSupplier(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Suppliers WHERE SupplierID = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}