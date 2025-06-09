package com.inventory.ui;

import com.inventory.dao.UsersDAOImpl;
import com.inventory.model.User;
import com.inventory.util.UserAuthentication;
import com.inventory.dao.LoginHistoryDAO;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class LoginScreen extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private Point mouseClickPoint;
    private LoginHistoryDAO loginHistoryDAO;

    private final Color primaryColor = new Color(63, 114, 175);
    private final Color backgroundColor = new Color(240, 240, 240);
    private final Color foregroundColor = new Color(30, 30, 30);
    private final Font defaultFont = new Font("Segoe UI", Font.PLAIN, 16);
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private JCheckBox showPasswordCheckBox; // Added Checkbox
    private Timer messageTimer;

    public LoginScreen() {
        loginHistoryDAO = new LoginHistoryDAO();

        setUndecorated(true);
        setSize(500, 420);
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBounds(0, 0, 500, 420);
        mainPanel.setBackground(backgroundColor);
        add(mainPanel);

        // Header Panel
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBounds(0, 0, 500, 60);
        headerPanel.setBackground(primaryColor);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(20, 15, 200, 30);
        headerPanel.add(titleLabel);

        JButton minimizeButton = createWindowButton("−", Color.WHITE);
        minimizeButton.setBounds(400, 15, 30, 30);
        minimizeButton.addActionListener(e -> setState(Frame.ICONIFIED));
        headerPanel.add(minimizeButton);

        JButton closeButton = createWindowButton("×", Color.WHITE);
        closeButton.setBounds(440, 15, 30, 30);
        closeButton.addActionListener(e -> {
            try {
                String username = usernameField.getText();
                UsersDAOImpl userDAO = new UsersDAOImpl();
                User user = userDAO.getUserByUsername(username);

                if (user != null) {
                    loginHistoryDAO.updateLogout(user.getUserID());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating logout time: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                System.exit(0);
            }
        });
        headerPanel.add(closeButton);
        mainPanel.add(headerPanel);

        // Content Panel
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBounds(50, 90, 400, 280);
        contentPanel.setBackground(backgroundColor);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(defaultFont);
        usernameLabel.setForeground(foregroundColor);
        usernameLabel.setBounds(50, 30, 100, 25);
        contentPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(defaultFont);
        usernameField.setBorder(new EmptyBorder(10, 10, 10, 10));
        usernameField.setBounds(150, 30, 200, 40);
        contentPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(defaultFont);
        passwordLabel.setForeground(foregroundColor);
        passwordLabel.setBounds(50, 90, 100, 25);
        contentPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(defaultFont);
        passwordField.setBorder(new EmptyBorder(10, 10, 10, 10));
        passwordField.setBounds(150, 90, 200, 40);
        contentPanel.add(passwordField);

        //show password check box
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(defaultFont);
        showPasswordCheckBox.setForeground(foregroundColor);
        showPasswordCheckBox.setBounds(150, 135, 200, 25);
        showPasswordCheckBox.setBackground(backgroundColor);
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setEchoChar((char) 0); // Show password
                } else {
                    passwordField.setEchoChar('•'); // Hide password
                }
            }
        });
        contentPanel.add(showPasswordCheckBox);

        messageLabel = new JLabel("");
        messageLabel.setFont(defaultFont);
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(50, 150, 300, 25);
        contentPanel.add(messageLabel);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(primaryColor);
        loginButton.setBorder(new EmptyBorder(12, 40, 12, 40));
        loginButton.setBounds(100, 200, 200, 50);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> performLogin());
        contentPanel.add(loginButton);

        mainPanel.add(contentPanel);

        // Dragging functionality
        mainPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseClickPoint = e.getPoint();
            }
        });

        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point newLocation = getLocation();
                newLocation.translate(e.getX() - mouseClickPoint.x, e.getY() - mouseClickPoint.y);
                setLocation(newLocation);
            }
        });

        setVisible(true);
    }

    private JButton createWindowButton(String symbol, Color color) {
        JButton button = new JButton(symbol);
        button.setFont(new Font("Dialog", Font.BOLD, 16));
        button.setForeground(color);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            UsersDAOImpl userDAO = new UsersDAOImpl();
            User user = userDAO.getUserByUsername(username);
            int roleID = UserAuthentication.authenticateUser(username, password);
            if (roleID > 0) {
                messageLabel.setText("Login successful!");
                messageLabel.setForeground(new Color(46, 204, 113));

                // Insert login time into database
                loginHistoryDAO.insertLogin(user.getUserID());

                // Use a Timer to clear the messageLabel after 3 seconds
                messageTimer = new Timer();
                messageTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        messageLabel.setText(""); // Clear the message
                        messageTimer.cancel(); // Stop the timer
                    }
                }, 3000); // 3000 milliseconds = 3 seconds

                SwingUtilities.invokeLater(() -> {
                    new MainApplication(roleID, username, user);
                    dispose();
                });
            } else {
                messageLabel.setText("Invalid username or password.");
                messageLabel.setForeground(Color.RED);
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            messageLabel.setText("Error: " + ex.getMessage());
        }
    }
}
