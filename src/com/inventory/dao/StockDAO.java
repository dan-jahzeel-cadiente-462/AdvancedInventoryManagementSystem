/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.dao;

import com.inventory.model.Stock;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author USER
 */
public interface StockDAO {
    public Stock getStockById(int id) throws SQLException;
    
    public List<Stock> getAllStock() throws SQLException;
    
    public void addStock(Stock stock) throws SQLException;
    
    public void updateStock(Stock stock) throws SQLException;
    
    public void deleteStock(int id) throws SQLException;
    
    public int getProductStock(int productId) throws SQLException;
    
    /**
     * Updates the stock quantity for a given product.  This method assumes that you
     * are tracking the *total* stock, not individual additions/removals.  It finds the most
     * recent stock entry for the product and updates its quantity.
     *
     * @param productId The ID of the product to update.
     * @param newQuantity The new total stock quantity.
     * @throws SQLException If a database error occurs.
     */
    public void updateStockQuantity(int productId, int newQuantity) throws SQLException;
    
    /**
     * Retrieves the stock entry for a given product and supplier.
     *
     * @param productId The ID of the product.
     * @param supplierId The ID of the supplier.
     * @return The Stock object if found, null otherwise.
     * @throws SQLException If a database error occurs.
     */
    public Stock getStockByProductAndSupplier(int productId, int supplierId) throws SQLException;
}
