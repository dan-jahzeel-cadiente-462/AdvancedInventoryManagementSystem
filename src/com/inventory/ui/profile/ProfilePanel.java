package com.inventory.ui.profile;

import com.inventory.dao.RolesDAOImpl;
import com.inventory.dao.UsersDAOImpl;
import com.inventory.model.User;
import com.inventory.ui.config.GUIConstants;
import com.inventory.ui.dashboard.DashboardPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JDialog;
import com.inventory.dao.LoginHistoryDAO;  // Import the LoginHistoryDAO
import com.inventory.model.LoginHistory; // Import the LoginHistory model
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class ProfilePanel extends JPanel {

    private JLabel userIdLabel;
    private JLabel usernameLabel;
    private JLabel roleNameLabel;
    private JLabel dateCreatedLabel;
    private JPanel innerPanel;
    private JPanel bottomPanel;
    private JLabel profileTitle;
    private boolean isViewOnly;
    private RolesDAOImpl roleDAO;
    private JLabel dateTimeLabel;
    private JButton editProfileButton;
    private JButton logoutButton;
    private User user;
    private UsersDAOImpl usersDAO;

    // Edit Profile Dialog Components
    private JDialog editDialog;
    private JTextField editUsernameField;
    private JComboBox<String> editRoleComboBox;
    private JTextField editPasswordField; // Added password field
    private JButton saveButton;
    private JButton cancelButton;

    // Login History Components
    private JPanel loginHistoryPanel;
    private JTable loginHistoryTable; // Change to JTable
    private DefaultTableModel loginHistoryTableModel;
    private JScrollPane loginHistoryScrollPane;
    private JButton viewLoginHistoryButton;
    private LoginHistoryDAO loginHistoryDAO;

    public ProfilePanel(boolean viewOnly, User user) {
        this.isViewOnly = viewOnly;
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(GUIConstants.BACKGROUND_COLOR);

        // Initialize labels
        userIdLabel = new JLabel("ID:");
        usernameLabel = new JLabel("Username:");
        roleNameLabel = new JLabel("Role/Position:");
        dateCreatedLabel = new JLabel("Date Created:");
        profileTitle = new JLabel("PROFILE");
        dateTimeLabel = new JLabel();
        dateTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel = new JPanel();

        profileTitle.setFont(GUIConstants.TITLE_FONT);

        innerPanel = new JPanel();
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.weightx = 0.5;

        // Add components to innerPanel
        addComponentToPanel(0, 0, GridBagConstraints.LINE_END, gbc, profileTitle, innerPanel);
        addComponentToPanel(0, 1, GridBagConstraints.LINE_END, gbc, usernameLabel, innerPanel);
        addComponentToPanel(0, 2, GridBagConstraints.LINE_END, gbc, userIdLabel, innerPanel);
        addComponentToPanel(0, 3, GridBagConstraints.LINE_END, gbc, roleNameLabel, innerPanel);
        addComponentToPanel(0, 4, GridBagConstraints.LINE_END, gbc, dateCreatedLabel, innerPanel);
        //addComponentToPanel(1, 5, GridBagConstraints.CENTER, gbc, dateTimeLabel, innerPanel); // Date/Time at the bottom, centered

        try {
            roleDAO = new RolesDAOImpl();
            String role = roleDAO.getRoleNameById(user.getUserID());
            String username = user.getUsername();
            int id = user.getUserID();
            // Date dateCreated = user.getDateCreated();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // String formattedDate = (dateCreated != null) ? sdf.format(dateCreated) : "N/A";

            addComponentToPanel(1, 1, GridBagConstraints.LINE_START, gbc, new JLabel(username), innerPanel);
            addComponentToPanel(1, 2, GridBagConstraints.LINE_START, gbc, new JLabel(String.valueOf(id)), innerPanel);
            addComponentToPanel(1, 3, GridBagConstraints.LINE_START, gbc, new JLabel(String.valueOf(role)), innerPanel);
            // addComponentToPanel(1, 4, GridBagConstraints.LINE_START, gbc, new JLabel(formattedDate), innerPanel);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

        // Initialize buttons
        editProfileButton = new JButton("Edit Profile");
        logoutButton = new JButton("Logout");
        viewLoginHistoryButton = new JButton("View Login History"); // Initialize the new button

        // Style buttons
        editProfileButton.setBackground(GUIConstants.ACCENT_COLOR);
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setFont(GUIConstants.BUTTON_FONT);
        editProfileButton.setBorder(new EmptyBorder(8, 16, 8, 16));
        editProfileButton.setFocusPainted(false);

        logoutButton.setBackground(GUIConstants.ACCENT_COLOR);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(GUIConstants.BUTTON_FONT);
        logoutButton.setBorder(new EmptyBorder(8, 16, 8, 16));
        logoutButton.setFocusPainted(false);

        viewLoginHistoryButton.setBackground(GUIConstants.ACCENT_COLOR);
        viewLoginHistoryButton.setForeground(Color.WHITE);
        viewLoginHistoryButton.setFont(GUIConstants.BUTTON_FONT);
        viewLoginHistoryButton.setBorder(new EmptyBorder(8, 16, 8, 16));
        viewLoginHistoryButton.setFocusPainted(false);

        // Add buttons to button panel
        buttonPanel.add(editProfileButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(viewLoginHistoryButton); // Add the new button

        // Add button panel
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        innerPanel.add(buttonPanel, gbc);
        
        DashboardPanel dashboardPanel = new DashboardPanel(user.getUserID());
        bottomPanel.add(dashboardPanel);

        add(innerPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize the UsersDAO and LoginHistoryDAO
        usersDAO = new UsersDAOImpl();
        loginHistoryDAO = new LoginHistoryDAO();  // Initialize LoginHistoryDAO

        // Set up the Edit Profile Dialog
        setupEditProfileDialog();

        // Set up Login History Panel
        setupLoginHistoryPanel();

        // ActionListener for Edit Profile Button
        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditProfileDialog();
            }
        });

        // ActionListener for Logout Button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        ProfilePanel.this,
                        "Are you sure you want to log out?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Logging out...");
                    //  You would typically clear the user session,
                    //  return to the login screen, or close the application.
                    // Update the logout time in the database
                    try {
                        loginHistoryDAO.updateLogout(user.getUserID());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(ProfilePanel.this, "Error updating logout time: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // ActionListener for View Login History Button
        viewLoginHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayLoginHistory();
            }
        });

        // Update date and time
        /*
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = sdf.format(new Date());
                dateTimeLabel.setText(formattedDateTime);
            }
        }, 0, 1000);
        */
    }

    private void addComponentToPanel(int gridx, int gridy, int anchor, GridBagConstraints gbc, JComponent component, JPanel panel) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.anchor = anchor;
        panel.add(component, gbc);
    }

    private void setupEditProfileDialog() {
        editDialog = new JDialog((Frame) null, "Edit Profile", true); // Make it modal
        editDialog.setLayout(new GridBagLayout());
        editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        editDialog.getContentPane().setBackground(GUIConstants.BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 0.5;

        // Username Label and Field
        JLabel usernameLabel = new JLabel("Username:");
        editUsernameField = new JTextField(20);
        addComponentToPanel(0, 0, GridBagConstraints.LINE_END, gbc, usernameLabel, (JPanel) editDialog.getContentPane());
        addComponentToPanel(1, 0, GridBagConstraints.LINE_START, gbc, editUsernameField, (JPanel) editDialog.getContentPane());

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        editPasswordField = new JTextField(20);
        addComponentToPanel(0, 1, GridBagConstraints.LINE_END, gbc, passwordLabel, (JPanel) editDialog.getContentPane());
        addComponentToPanel(1, 1, GridBagConstraints.LINE_START, gbc, editPasswordField, (JPanel) editDialog.getContentPane());

        // Role Label and ComboBox
        JLabel roleLabel = new JLabel("Role:");
        editRoleComboBox = new JComboBox<>();
        try {
            RolesDAOImpl rolesDAO = new RolesDAOImpl();
            List<String> roleNames = rolesDAO.getAllRoleNames(); // Method to get all role names
            for (String roleName : roleNames) {
                editRoleComboBox.addItem(roleName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching roles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        addComponentToPanel(0, 2, GridBagConstraints.LINE_END, gbc, roleLabel, (JPanel) editDialog.getContentPane());
        addComponentToPanel(1, 2, GridBagConstraints.LINE_START, gbc, editRoleComboBox, (JPanel) editDialog.getContentPane());

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

        saveButton = new JButton("Save");
        saveButton.setBackground(GUIConstants.ACCENT_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(GUIConstants.BUTTON_FONT);
        saveButton.setBorder(new EmptyBorder(8, 16, 8, 16));
        saveButton.setFocusPainted(false);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(GUIConstants.ACCENT_COLOR);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(GUIConstants.BUTTON_FONT);
        cancelButton.setBorder(new EmptyBorder(8, 16, 8, 16));
        cancelButton.setFocusPainted(false);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editDialog.getContentPane().add(buttonPanel, gbc);

        // Add listeners to the buttons
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDialog.dispose();
            }
        });

        editDialog.pack();
        editDialog.setLocationRelativeTo(this); // Center relative to the main panel

    }

    private void openEditProfileDialog() {
        // Populate the dialog fields with the current user data
        editUsernameField.setText(user.getUsername());
        editRoleComboBox.setSelectedItem(getRoleName(user.getUserID())); // Set the correct role name
        editPasswordField.setText(""); // Clear or set the existing password

        editDialog.setVisible(true);
    }

    private void saveChanges() {
        try {
            // Get the updated user data from the dialog fields
            String updatedUsername = editUsernameField.getText();
            String updatedPassword = editPasswordField.getText(); // Get password
            String updatedRoleName = (String) editRoleComboBox.getSelectedItem();

            // Get the role ID based on the selected role name.
            int updatedRoleId = getRoleIdByName(updatedRoleName);

            // Basic validation
            if (updatedUsername.isEmpty() || updatedPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update the user object
            user.setUsername(updatedUsername);
            user.setPassword(updatedPassword); // In real app, hash this.
            user.setRoleID(updatedRoleId);

            // Update the user in the database
            usersDAO.updateUser(user);

            // Update the display on the ProfilePanel
            updateProfileDisplay();

            JOptionPane.showMessageDialog(this, "Profile updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            editDialog.dispose(); // Close the dialog
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProfileDisplay() {
        try {
            // Get the role name
            String roleName = roleDAO.getRoleNameById(user.getUserID());

            // update the fields.
            usernameLabel.setText("Username: " + user.getUsername());
            roleNameLabel.setText("Role/Position: " + roleName);
            userIdLabel.setText("ID: " + user.getUserID());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get Role ID from Role Name
    private int getRoleIdByName(String roleName) {
        try {
            RolesDAOImpl rolesDAO = new RolesDAOImpl();
            return rolesDAO.getRoleIdByName(roleName);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Or throw an exception
        }
    }

    private String getRoleName(int roleId) {
        try {
            RolesDAOImpl rolesDAO = new RolesDAOImpl();
            return rolesDAO.getRoleNameById(roleId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    private void setupLoginHistoryPanel() {
        loginHistoryPanel = new JPanel(new BorderLayout());
        loginHistoryPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
        loginHistoryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialize table and model
        loginHistoryTableModel = new DefaultTableModel(
                new Object[]{"Login Time", "Logout Time"}, 0); // Columns
        loginHistoryTable = new JTable(loginHistoryTableModel);
        loginHistoryScrollPane = new JScrollPane(loginHistoryTable);
        loginHistoryPanel.add(loginHistoryScrollPane, BorderLayout.CENTER);
        loginHistoryTable.setEnabled(false); // Make the table read-only
    }

    private void displayLoginHistory() {
        try {
            // Fetch the login history for the current user
            List<LoginHistory> history = getLoginHistory(user.getUserID());
            loginHistoryTableModel.setRowCount(0); // Clear previous history

            if (history.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No login history found for this user.",
                        "No History",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (LoginHistory record : history) {
                    String loginTime = sdf.format(record.getLoginTime());
                    String logoutTime = (record.getLogoutTime() != null)
                            ? sdf.format(record.getLogoutTime())
                            : "Not Logged Out";
                    loginHistoryTableModel.addRow(new Object[]{loginTime, logoutTime});
                }

                // Show the login history in a dialog
                JOptionPane.showMessageDialog(
                        this,
                        loginHistoryPanel,
                        "Login History",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error retrieving login history: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<LoginHistory> getLoginHistory(int userId) throws SQLException {
        return loginHistoryDAO.getLoginHistoryByUserId(userId);
    }
}

