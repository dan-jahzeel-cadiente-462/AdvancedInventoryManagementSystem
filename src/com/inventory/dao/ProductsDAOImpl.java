package com.inventory.dao;

import com.inventory.model.Product;
// import com.inventory.model.User;
import com.inventory.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDAOImpl implements ProductsDAO {
    
    @Override
    public Product getProductById(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Products WHERE ProductID = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("ProductID"));
                product.setName(rs.getString("Name"));
                product.setCategory(rs.getString("Category"));
                product.setPrice(rs.getDouble("Price"));
                product.setUserID(rs.getInt("userID"));
                return product;
            }
            return null;
        }
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT ProductID, Name, Category, Price, UserID FROM Products")) { //changed the query to include UserID
            ResultSet rs = pstmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("ProductID"));
                product.setName(rs.getString("Name"));
                product.setCategory(rs.getString("Category"));
                product.setPrice(rs.getDouble("Price"));
                product.setUserID(rs.getInt("userID")); //added this line
                products.add(product);
            }
            return products;
        }
    }

    /**
     * 
     * @param product The object
     * @param userId The ID of the user.
     * @throws SQLException If the database error occurs.
     */
    @Override
    public void addProduct(Product product, int userId) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO Products (Name, Category, Price, UserID) VALUES (?, ?, ?, ?)")) { //added userID
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, userId); // Set the UserID
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateProduct(Product product) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE Products SET Name = ?, Category = ?, Price = ? WHERE ProductID = ?")) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getProductID());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteProduct(int id) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Products WHERE ProductID = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Allows the user to search keywords to find exact matches.
     * 
     * @param searchTerm The String for filtering keywords.
     * @return The product object if found.
     * @throws SQLException If the database error occurs.
     */
    @Override
    public List<Product> searchProducts(String searchTerm) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM Products WHERE Name LIKE ? OR Category LIKE ?")) {
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("ProductID"));
                product.setName(rs.getString("Name"));
                product.setCategory(rs.getString("Category"));
                product.setPrice(rs.getDouble("Price"));
                products.add(product);
            }
            return products;
        }
    }
    
    @Override
    public List<Product> filterPrices(double searchTerm) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Products WHERE Price = ?")) {
            pstmt.setDouble(1, searchTerm);
            ResultSet rs = pstmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("ProductID"));
                product.setName(rs.getString("Name"));
                product.setCategory(rs.getString("Category"));
                product.setPrice(rs.getDouble("Price"));
                products.add(product);
            }
            return products;
        }
    }

     public int getProductsAddedByUserId(int userId) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM Products WHERE UserID = ?")) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // Returns the count
            }
            return 0;
        }
    }
}

