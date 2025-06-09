package com.inventory.dao;

import com.inventory.model.Reports;
import com.inventory.model.Reports;
import com.inventory.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportsDAO {

    /**
     * Calculates and retrieves sales revenue per product and supplier contributions.
     *
     * @return A list of Reports objects containing the report data.
     * @throws SQLException if a database error occurs
     */
    public List<Reports> getProductSupplierSalesReport() throws SQLException {
        List<Reports> reports = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT " +
                             "p.Name AS ProductName, " +
                             "s.Name AS SupplierName, " +
                             "SUM(sa.TotalAmount) AS TotalRevenue, " +
                             "SUM(sa.TotalAmount) / (SELECT SUM(TotalAmount) FROM Sales) AS SupplierContribution " +
                             "FROM Sales sa " +
                             "JOIN Products p ON sa.ProductID = p.ProductID " +
                             "JOIN Stock st ON p.ProductID = st.ProductID " +
                             "JOIN Suppliers s ON st.SupplierID = s.SupplierID " +
                             "GROUP BY p.ProductID, s.SupplierID, p.Name, s.Name " + // Include p.Name and s.Name in GROUP BY
                             "ORDER BY p.Name, s.Name")) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Reports report = new Reports();
                report.setProductName(rs.getString("ProductName"));
                report.setSupplierName(rs.getString("SupplierName"));
                report.setTotalRevenue(rs.getDouble("TotalRevenue"));
                report.setSupplierContribution(rs.getDouble("SupplierContribution"));
                reports.add(report);
            }
        }
        return reports;
    }
}