package com.inventory.ui.user;

import com.inventory.dao.UsersDAOImpl;
import com.inventory.model.User;
import com.inventory.ui.MainApplication;
import com.inventory.ui.config.GUIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class UserManagementPanel extends JPanel {

    private final UsersDAOImpl usersDAO;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField;
    private JPanel topPanel;
    private JPasswordField passwordField;
    private JComboBox<Integer> roleComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    
    // Sorting functionality
    private JPanel filterPanel;
    private JRadioButton adminRadioButton;
    private JRadioButton staffRadioButton;
    private JRadioButton defaultRadioButton;
    private JLabel filterLabel;
    private ButtonGroup filterButtonGroup;
    
    private JPanel searchPanel;
    private JLabel searchLabel;
    private JTextField searchField;
    private JButton searchButton;
    
    private static final String SORT_BY_USERNAME_ASC = "Userame (Asc)"; // Name in ascending order
    private static final String SORT_BY_USERNAME_DESC = "Username (Desc)"; // Name in descending order
    private static final String FILTER_BY_ROLE = "Filter (Role)";

    // Colors - Consistent with MainApplication
    private static final Color BACKGROUND_COLOR = new Color(245, 250, 255);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color BUTTON_HOVER_COLOR = new Color(41, 128, 185);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color BORDER_COLOR = new Color(204, 204, 204);

    // Fonts - Consistent with MainApplication
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TEXT_FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font MESSAGE_FONT = new Font("Segoe UI", Font.ITALIC, 12);

    public UserManagementPanel(boolean viewOnly, User user) {
        usersDAO = new UsersDAOImpl();
        initializeUI();
        loadUsers();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Table setup
        tableModel = new DefaultTableModel();
        userTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(userTable);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);

        // Labels
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(LABEL_FONT);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(LABEL_FONT);
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(LABEL_FONT);

        // Input fields
        usernameField = new JTextField(20);
        usernameField.setFont(TEXT_FIELD_FONT);
        passwordField = new JPasswordField(20);
        passwordField.setFont(TEXT_FIELD_FONT);
        roleComboBox = new JComboBox<>(new Integer[]{1, 2}); // Assuming roles are 1 and 2
        roleComboBox.setFont(TEXT_FIELD_FONT);

        // Buttons
        addButton = new JButton("Add");
        addButton.setFont(BUTTON_FONT);
        addButton.setBackground(ACCENT_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        updateButton = new JButton("Update");
        updateButton.setFont(BUTTON_FONT);
        updateButton.setBackground(ACCENT_COLOR);
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.setFont(BUTTON_FONT);
        deleteButton.setBackground(ACCENT_COLOR);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        
        
        searchPanel = new JPanel();
        searchPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
        searchPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        searchPanel.setLayout(new FlowLayout());
        
        searchLabel = new JLabel("Enter keywords:");
        searchPanel.add(searchLabel);
        
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(360, 25));
        searchPanel.add(searchField);
        
        searchButton = new JButton("SEARCH");
        searchPanel.add(searchButton);
        
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchUsers(searchField.getText());
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchUsers(searchField.getText());
            }
        });
        
        
        filterPanel = new JPanel();
        filterPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
        filterPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        // setPreferredSize(new Dimension(100, 80));
        filterLabel = new JLabel("Filters:");
        adminRadioButton = new JRadioButton("Admin");
        staffRadioButton = new JRadioButton("Staff");
        defaultRadioButton = new JRadioButton("Default");      
        filterButtonGroup = new ButtonGroup();
        filterButtonGroup.add(adminRadioButton);
        filterButtonGroup.add(staffRadioButton);
        filterButtonGroup.add(defaultRadioButton);       
        defaultRadioButton.setSelected(true);
        
        filterPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(filterLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        filterPanel.add(adminRadioButton, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        filterPanel.add(staffRadioButton, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        filterPanel.add(defaultRadioButton, gbc);
        
        adminRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        
        staffRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        
        defaultRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });
        
        // Layout using GridBagConstraints
        GridBagConstraints gbcSort = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        formPanel.add(buttonPanel, gbc);

        // Add components to the panel
        add(tableScrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);
        
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        /*
        topPanel.add(new UserSearchPanel());
        topPanel.add(new UserFilteringPanel());
        */
        
        topPanel.setLayout(new FlowLayout());
        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.insets = new Insets(10, 10, 10, 10);
        gbcTop.fill = GridBagConstraints.HORIZONTAL;
        // topPanel.setLayout(new FlowLayout());
        
        gbcTop.gridx = 0;
        gbcTop.gridy = 0;
        gbcTop.anchor = GridBagConstraints.WEST;
        topPanel.add(searchPanel);
        
        gbcTop.gridx = 1;
        gbcTop.gridx = 0;
        // gbcTop.anchor = GridBagConstraints.LINE_END;
        
        add(topPanel, BorderLayout.NORTH);
    }

    private void loadUsers() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        tableModel.addColumn("User ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Role");
        styleTable();

        // Create a map to store role IDs and their corresponding names
        Map<Integer, String> roleMap = new HashMap<>();
        roleMap.put(1, "Admin");
        roleMap.put(2, "Staff");
        // Add more roles as needed

        try {
            List<User> users = usersDAO.getAllUsers();
            for (User user : users) {
                // Get the role name from the map using the role ID
                String roleName = roleMap.get(user.getRoleID());
                // If the role ID is not found in the map, display "Unknown"
                if (roleName == null) {
                    roleName = "Unknown";
                }
                tableModel.addRow(new Object[]{user.getUserID(), user.getUsername(), roleName});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword()); // In real app, hash this!
        int role = (Integer) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRoleID(role);

        try {
            usersDAO.addUser(newUser);
            loadUsers(); // Refresh the table
            clearFields();
            JOptionPane.showMessageDialog(this, "User added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());  // In real app, hash this!
        int role = (Integer) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User updatedUser = new User();
        updatedUser.setUserID(userId);
        updatedUser.setUsername(username);
        updatedUser.setPassword(password);
        updatedUser.setRoleID(role);

        try {
            usersDAO.updateUser(updatedUser);
            loadUsers();
            clearFields();
            JOptionPane.showMessageDialog(this, "User updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (Integer) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                usersDAO.deleteUser(userId);
                loadUsers();
                clearFields();
                JOptionPane.showMessageDialog(this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void styleTable() {
        userTable.setBackground(BACKGROUND_COLOR);
        userTable.setForeground(TEXT_COLOR);
        userTable.setGridColor(BORDER_COLOR);
        userTable.setRowHeight(30);
        userTable.getTableHeader().setBackground(BACKGROUND_COLOR);
        userTable.getTableHeader().setForeground(TEXT_COLOR);
        userTable.setSelectionBackground(ACCENT_COLOR);
        userTable.setSelectionForeground(Color.WHITE);
        userTable.setFont(TEXT_FIELD_FONT); // Use the same font as text fields

        // Center align the text in the columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < userTable.getColumnCount(); i++) {
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    private void searchUsers(String keyword) {
        try {
            UsersDAOImpl userDAO = new UsersDAOImpl();
            List<User> users = userDAO.searchUsers(keyword);
            tableModel.setRowCount(0);
            for (User user : users) {
                tableModel.addRow(new Object[]{
                    user.getUserID(),
                    user.getUsername(),
                    user.getRoleID()
                });
            }
        } catch (SQLException e) {
            // showMessage("Error searching users: " + e.getMessage(), false);
        }
    }
    
    private void sortUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            User user = new User(
                    (int) tableModel.getValueAt(i, 0),
                    (String) tableModel.getValueAt(i, 1),
                    (int) tableModel.getValueAt(i, 2)
            );
            users.add(user);
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
    }
}

