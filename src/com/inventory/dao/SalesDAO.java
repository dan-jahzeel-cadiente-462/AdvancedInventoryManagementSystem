/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.dao;

import com.inventory.model.Sale;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author USER
 */
public interface SalesDAO {
    public Sale getSaleById(int id) throws SQLException;
    public List<Sale> getAllSales() throws SQLException;
    public void addSale(Sale sale) throws SQLException;
    public void updateSale(Sale sale) throws SQLException;
    public void deleteSale(int id) throws SQLException;
    public double getTotalSalesRevenue(int productId) throws SQLException;
    public double getTotalSalesRevenueByUserId(int userId) throws SQLException;
    public int getSalesCountByUserId(int userId) throws SQLException;
}
