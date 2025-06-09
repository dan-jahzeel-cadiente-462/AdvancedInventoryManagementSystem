package com.inventory.ui.dashboard;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.EmptyBorder;
import java.sql.SQLException;
import com.inventory.dao.ProductsDAOImpl;
import com.inventory.dao.SalesDAOImpl;

public class DashboardPanel extends JPanel {

    private JLabel productsAddedLabel;
    private JLabel totalSalesLabel;
    private int userId;
    private ProductsDAOImpl productsDAO;
    private SalesDAOImpl salesDAO;
    private Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private Color TEXT_COLOR = new Color(51, 51, 51);
    

    public DashboardPanel(int userId) {
        this.userId = userId;
        productsDAO = new ProductsDAOImpl();
        salesDAO = new SalesDAOImpl();

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 0, 0, 0));

        productsAddedLabel = new JLabel("Products Added: 0");
        productsAddedLabel.setFont(LABEL_FONT);
        productsAddedLabel.setForeground(TEXT_COLOR);
        totalSalesLabel = new JLabel("Total Sales: ₱0.00");
        totalSalesLabel.setFont(LABEL_FONT);
        totalSalesLabel.setForeground(TEXT_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(productsAddedLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(totalSalesLabel, gbc);

        loadDashboardData();
    }

    private void loadDashboardData() {
        try {
            int numProductsAdded = productsDAO.getProductsAddedByUserId(userId);
            double totalSales = salesDAO.getTotalSalesRevenueByUserId(userId);

            productsAddedLabel.setText("Products Added: " + numProductsAdded);
            totalSalesLabel.setText("Total Sales: ₱" + String.format("%.2f", totalSales));

        } catch (SQLException e) {
            e.printStackTrace();
            // In a real application, handle this error more gracefully
            productsAddedLabel.setText("Error loading data");
            totalSalesLabel.setText("Error loading data");
        }
    }
}

