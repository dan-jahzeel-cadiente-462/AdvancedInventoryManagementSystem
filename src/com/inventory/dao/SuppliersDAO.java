/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.dao;

import com.inventory.model.Supplier;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author USER
 */
public interface SuppliersDAO {
    public Supplier getSupplierById(int id) throws SQLException;
    public List<Supplier> getAllSuppliers() throws SQLException;
    public List<String> getAllSuppliersByName() throws SQLException;
    public void addSupplier(Supplier supplier) throws SQLException;
    public void updateSupplier(Supplier supplier) throws SQLException;
    public void deleteSupplier(int id) throws SQLException;
}
