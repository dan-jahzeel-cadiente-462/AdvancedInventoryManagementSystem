/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.inventory.dao;

import com.inventory.model.Product;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author USER
 */
public interface ProductsDAO {
    public Product getProductById(int id) throws SQLException;
    public List<Product> getAllProducts() throws SQLException;
    public void addProduct(Product product, int userId) throws SQLException;
    public void updateProduct(Product product) throws SQLException;
    public void deleteProduct(int id) throws SQLException;
    public List<Product> searchProducts(String searchTerm) throws SQLException;
    public List<Product> filterPrices(double searchTerm) throws SQLException;
}
