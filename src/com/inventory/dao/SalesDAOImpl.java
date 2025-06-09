package com.inventory.dao;

import com.inventory.model.Sale;
import com.inventory.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAOImpl implements SalesDAO {

    @Override
    public Sale getSaleById(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Sales WHERE SaleID = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Sale sale = new Sale();
                sale.setSaleID(rs.getInt("SaleID"));
                sale.setProductID(rs.getInt("ProductID"));
                sale.setQuantitySold(rs.getInt("QuantitySold"));
                sale.setSaleDate(rs.getDate("SaleDate"));
                sale.setTotalAmount(rs.getDouble("TotalAmount"));
                return sale;
            }
            return null;
        }
    }

    @Override
    public List<Sale> getAllSales() throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Sales")) {
            ResultSet rs = pstmt.executeQuery();
            List<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setSaleID(rs.getInt("SaleID"));
                sale.setProductID(rs.getInt("ProductID"));
                sale.setQuantitySold(rs.getInt("QuantitySold"));
                sale.setSaleDate(rs.getDate("SaleDate"));
                sale.setTotalAmount(rs.getDouble("TotalAmount"));
                sales.add(sale);
            }
            return sales;
        }
    }

    @Override
    public void addSale(Sale sale) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO Sales (ProductID, QuantitySold, SaleDate, TotalAmount) VALUES (?, ?, ?, ?)")) {
            pstmt.setInt(1, sale.getProductID());
            pstmt.setInt(2, sale.getQuantitySold());
            pstmt.setDate(3, new java.sql.Date(sale.getSaleDate().getTime()));
            pstmt.setDouble(4, sale.getTotalAmount());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateSale(Sale sale) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE Sales SET ProductID = ?, QuantitySold = ?, SaleDate = ?, TotalAmount = ? WHERE SaleID = ?")) {
            pstmt.setInt(1, sale.getProductID());
            pstmt.setInt(2, sale.getQuantitySold());
            pstmt.setDate(3, new java.sql.Date(sale.getSaleDate().getTime()));
            pstmt.setDouble(4, sale.getTotalAmount());
            pstmt.setInt(5, sale.getSaleID());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteSale(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Sales WHERE SaleID = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Additional methods for reporting
    @Override
    public double getTotalSalesRevenue(int productId) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT SUM(TotalAmount) FROM Sales WHERE ProductID = ?")) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1); // Get the sum from the first column
            }
            return 0.0;
        }
    }

    @Override
    public double getTotalSalesRevenueByUserId(int userId) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT SUM(s.TotalAmount) FROM Sales s " +
                             "JOIN Products p ON s.ProductID = p.ProductID " +
                             "WHERE p.UserID = ?")) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
            return 0.0;
        }
    }

    @Override
    public int getSalesCountByUserId(int userId) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(s.SaleID) FROM Sales s " +
                             "JOIN Products p ON s.ProductID = p.ProductID " +
                             "WHERE p.UserID = ?")) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
}

