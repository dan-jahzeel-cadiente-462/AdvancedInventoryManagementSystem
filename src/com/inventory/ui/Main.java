package com.inventory.ui;

import com.inventory.ui.LoginScreen;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater() to ensure that the GUI is created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            new LoginScreen(); // Create and display the login screen
        });
    }
}
