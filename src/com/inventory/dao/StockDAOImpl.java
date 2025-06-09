package com.inventory.dao;

import com.inventory.model.Stock;
import com.inventory.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAOImpl implements StockDAO {

    /**
     * 
     * @param id The ID for a given stock.
     * @return null
     * @throws SQLException 
     */
    @Override
    public Stock getStockById(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Stock WHERE StockID = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Stock stock = new Stock();
                stock.setStockID(rs.getInt("StockID"));
                stock.setProductID(rs.getInt("ProductID"));
                stock.setSupplierID(rs.getInt("SupplierID"));
                stock.setQuantityAdded(rs.getInt("QuantityAdded"));
                stock.setDateAdded(rs.getDate("DateAdded"));
                return stock;
            }
            return null;
        }
    }

    @Override
    public List<Stock> getAllStock() throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Stock")) {
            ResultSet rs = pstmt.executeQuery();
            List<Stock> stocks = new ArrayList<>();
            while (rs.next()) {
                Stock stock = new Stock();
                stock.setStockID(rs.getInt("StockID"));
                stock.setProductID(rs.getInt("ProductID"));
                stock.setSupplierID(rs.getInt("SupplierID"));
                stock.setQuantityAdded(rs.getInt("QuantityAdded"));
                stock.setDateAdded(rs.getDate("DateAdded"));
                stocks.add(stock);
            }
            return stocks;
        }
    }

    @Override
    public void addStock(Stock stock) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO Stock (ProductID, SupplierID, QuantityAdded, DateAdded) VALUES (?, ?, ?, ?)")) {
            pstmt.setInt(1, stock.getProductID());
            pstmt.setInt(2, stock.getSupplierID());
            pstmt.setInt(3, stock.getQuantityAdded());
            pstmt.setDate(4, new java.sql.Date(stock.getDateAdded().getTime()));
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateStock(Stock stock) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE Stock SET ProductID = ?, SupplierID = ?, QuantityAdded = ?, DateAdded = ? WHERE StockID = ?")) {
            pstmt.setInt(1, stock.getProductID());
            pstmt.setInt(2, stock.getSupplierID());
            pstmt.setInt(3, stock.getQuantityAdded());
            pstmt.setDate(4, new java.sql.Date(stock.getDateAdded().getTime()));
            pstmt.setInt(5, stock.getStockID());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes the stock for a given product
     * 
     * @param id The ID of the stock.
     * @throws SQLException If a database occurs.
     */
    @Override
    public void deleteStock(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Stock WHERE StockID = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves the current stock quantity for a given product.
     *
     * @param productId The ID of the product.
     * @return The current stock quantity, or 0 if not found or error.
     * @throws SQLException If a database error occurs.
     */
    @Override
    public int getProductStock(int productId) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT QuantityAdded FROM Stock WHERE ProductID = ? ORDER BY DateAdded DESC LIMIT 1")) { //gets the last stock added
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("QuantityAdded");
            }
            return 0; // Return 0 if no stock found for the product
        }
    }

    /**
     * Updates the stock quantity for a given product.  This method assumes that you
     * are tracking the *total* stock, not individual additions/removals.  It finds the most
     * recent stock entry for the product and updates its quantity.
     *
     * @param productId The ID of the product to update.
     * @param newQuantity The new total stock quantity.
     * @throws SQLException If a database error occurs.
     */
    @Override
    public void updateStockQuantity(int productId, int newQuantity) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE Stock SET QuantityAdded = ? WHERE ProductID = ? ORDER BY DateAdded DESC LIMIT 1")) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves the stock entry for a given product and supplier.
     *
     * @param productId The ID of the product.
     * @param supplierId The ID of the supplier.
     * @return The Stock object if found, null otherwise.
     * @throws SQLException If a database error occurs.
     */
    @Override
    public Stock getStockByProductAndSupplier(int productId, int supplierId) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM Stock WHERE ProductID = ? AND SupplierID = ?")) {
            pstmt.setInt(1, productId);
            pstmt.setInt(2, supplierId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Stock stock = new Stock();
                stock.setStockID(rs.getInt("StockID"));
                stock.setProductID(rs.getInt("ProductID"));
                stock.setSupplierID(rs.getInt("SupplierID"));
                stock.setQuantityAdded(rs.getInt("QuantityAdded"));
                stock.setDateAdded(rs.getDate("DateAdded"));
                return stock;
            }
            return null;
        }
    }
}

