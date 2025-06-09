package com.inventory.ui;

/*
import com.inventory.ui.product.ProductManagementPanel;
import com.inventory.ui.supplier.SupplierManagementPanel;
import com.inventory.ui.stock.StockManagementPanel;
import com.inventory.ui.sales.SalesManagementPanel;
import com.inventory.ui.reports.ReportsPanel;
import com.inventory.ui.user.UserManagementPanel; // Import UserManagementPanel
*/

import com.inventory.dao.LoginHistoryDAO;
import com.inventory.ui.profile.ProfilePanel;
import com.inventory.ui.user.UserManagementPanel;
import com.inventory.ui.product.ProductManagementPanel;
import com.inventory.ui.supplier.SupplierManagementPanel;
import com.inventory.ui.stock.StockManagementPanel;
import com.inventory.ui.reports.ReportsPanel;

import com.inventory.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;
import com.inventory.dao.LoginHistoryDAO; // Import LoginHistoryDAO
import com.inventory.dao.UsersDAOImpl;
import com.inventory.ui.sale.SalesManagementPanel;

public class MainApplication extends JFrame {

    private final int userRole;
    private final String loggedInUsername;
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JMenu homeMenu;
    private JMenu userManagementMenu;
    private JMenu inventoryMenu;
    private JMenu settingsMenu;
    private CardLayout cardLayout;
    private Map<String, JPanel> panelMap;
    private User userData;
    private LoginHistoryDAO loginHistoryDAO; // Add LoginHistoryDAO instance
    
    private static final String VIEW_PROFILE_PANEL = "viewProfile";
    private static final String PRODUCT_MANAGEMENT_PANEL = "productManagementPanel";
    private static final String USER_MANAGEMENT_PANEL = "userManagementPanel";
    private static final String SUPPLIER_MANAGEMENT_PANEL = "supplierManagementPanel";
    private static final String STOCK_MANAGEMENT_PANEL = "stockManagementPanel";
    private static final String SALE_MANAGEMENT_PANEL = "saleManagementPanel";
    private static final String INVENTORY_REPORT_PANEL = "inventoryReportPanel";

    // Colors for the flat design - Consistent with LoginScreen
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240); // Light gray
    private static final Color PRIMARY_COLOR = new Color(63, 114, 175); // Darker blue
    private static final Color TEXT_COLOR = new Color(30, 30, 30);       // Very dark gray

    public MainApplication(int userRole, String loggedInUsername, User user) {
        this.userRole = userRole;
        this.loggedInUsername = loggedInUsername;
        this.userData = user;
        this.loginHistoryDAO = new LoginHistoryDAO(); // Initialize it here
        
        //System.out.println("MainApplication started. User Role: " + userRole + ", Username: " + loggedInUsername); //Removed

        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized

        createMenuBar();
        createCardLayoutPanels(user); // Pass the User object
        
        // Show the initial panel -  Moved to constructor.
        showPanel(VIEW_PROFILE_PANEL); // Show profile panel on startup
        
        setVisible(true);
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();

        // Home Menu
        homeMenu = new JMenu("Home");
        homeMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        menuBar.add(homeMenu);

        JMenuItem profileItem = new JMenuItem("View Profile");
        profileItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        homeMenu.add(profileItem);
        
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        homeMenu.add(logoutItem);

        // User Management Menu (Conditional)
        if (userRole == 1) { // 1 = Admin
            userManagementMenu = new JMenu("User Management");
            userManagementMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            menuBar.add(userManagementMenu);

            JMenuItem userListItem = new JMenuItem("User List");
            userListItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            userManagementMenu.add(userListItem);
            
            userListItem.addActionListener(e -> showPanel(USER_MANAGEMENT_PANEL));
        }

        // Inventory Menu
        inventoryMenu = new JMenu("Inventory");
        inventoryMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        menuBar.add(inventoryMenu);

        JMenuItem productItem = new JMenuItem("Products");
        productItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inventoryMenu.add(productItem);

        JMenuItem supplierItem = new JMenuItem("Suppliers");
        supplierItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inventoryMenu.add(supplierItem);

        JMenuItem stockItem = new JMenuItem("Stock");
        stockItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inventoryMenu.add(stockItem);

        // Sales Menu
        JMenu salesMenu = new JMenu("Sales");
        salesMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        menuBar.add(salesMenu);

        JMenuItem salesListItem = new JMenuItem("Sales List");
        salesListItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        salesMenu.add(salesListItem);

        // Reports Menu
        JMenu reportsMenu = new JMenu("Reports");
        reportsMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        menuBar.add(reportsMenu);

        JMenuItem inventoryReportItem = new JMenuItem("Inventory Report");
        inventoryReportItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reportsMenu.add(inventoryReportItem);

        // Settings Menu
        settingsMenu = new JMenu("Settings");
        settingsMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        menuBar.add(settingsMenu);

        JMenuItem systemSettingsItem = new JMenuItem("System Settings");
        systemSettingsItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        settingsMenu.add(systemSettingsItem);

        setJMenuBar(menuBar);
        
        profileItem.addActionListener(e -> showPanel(VIEW_PROFILE_PANEL));
        productItem.addActionListener(e -> showPanel(PRODUCT_MANAGEMENT_PANEL));
        supplierItem.addActionListener(e -> showPanel(SUPPLIER_MANAGEMENT_PANEL));
        stockItem.addActionListener(e -> showPanel(STOCK_MANAGEMENT_PANEL));
        salesListItem.addActionListener(e -> showPanel(SALE_MANAGEMENT_PANEL));
        inventoryReportItem.addActionListener(e -> showPanel(INVENTORY_REPORT_PANEL));
        
        logoutItem.addActionListener(e -> logout());
    }

    private void createCardLayoutPanels(User user) {
        // Create card layout panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add border for spacing
        panelMap = new HashMap<>();
        
        ProfilePanel profilePanel = new ProfilePanel(true, user);
        ProductManagementPanel productManagementPanel = new ProductManagementPanel(false, user);
        UserManagementPanel userManagementPanel = new UserManagementPanel(false, user);
        SupplierManagementPanel supplierManagementPanel = new SupplierManagementPanel(false);
        StockManagementPanel stockManagementPanel = new StockManagementPanel(false);
        SalesManagementPanel saleManagementPanel = new SalesManagementPanel(false);
        ReportsPanel reportPanel = new ReportsPanel();
        
        panelMap.put(VIEW_PROFILE_PANEL, profilePanel);
        panelMap.put(PRODUCT_MANAGEMENT_PANEL, productManagementPanel);
        panelMap.put(USER_MANAGEMENT_PANEL, userManagementPanel);
        panelMap.put(SUPPLIER_MANAGEMENT_PANEL, supplierManagementPanel);
        panelMap.put(STOCK_MANAGEMENT_PANEL, stockManagementPanel);
        panelMap.put(SALE_MANAGEMENT_PANEL, saleManagementPanel);
        panelMap.put(INVENTORY_REPORT_PANEL, reportPanel);
        
        mainPanel.add(profilePanel, VIEW_PROFILE_PANEL);
        mainPanel.add(productManagementPanel, PRODUCT_MANAGEMENT_PANEL);
        mainPanel.add(userManagementPanel, USER_MANAGEMENT_PANEL);
        mainPanel.add(supplierManagementPanel, SUPPLIER_MANAGEMENT_PANEL);
        mainPanel.add(stockManagementPanel, STOCK_MANAGEMENT_PANEL);
        mainPanel.add(saleManagementPanel, SALE_MANAGEMENT_PANEL);
        mainPanel.add(reportPanel, INVENTORY_REPORT_PANEL);
        
        add(mainPanel, BorderLayout.CENTER);
    }
        
    private void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        System.out.println("Showing panel: " + panelName);
    }


    private void logout() {
        try {
            // Update logout time in database
            loginHistoryDAO.updateLogout(userData.getUserID());
            //dispose(); // Close the current window.  -- Removed dispose()
            // start the Login Screen
            SwingUtilities.invokeLater(() -> {
                new LoginScreen().setVisible(true);
                dispose();
            });

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error logging out: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            //setVisible(false); // Hide the main application window.
            //System.exit(0);
        }
    }
}
