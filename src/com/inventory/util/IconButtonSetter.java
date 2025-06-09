/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.util;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconButtonSetter {

    /**
     * Sets the icon on the provided JButton.
     *
     * @param button    The JButton to set the icon on.
     * @param iconPath  The path to the image file for the icon.
     * This can be a relative path from the project's root
     * or an absolute path. For resources within the JAR,
     * use getClass().getResource("/path/to/your/icon.png").toString().
     */
    public static void setButtonIcon(JButton button, String iconPath) {
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            button.setIcon(icon);
            // Optionally, remove the text if you only want to display the icon
            // button.setText("");
        } catch (Exception e) {
            System.err.println("Error loading icon: " + iconPath);
            e.printStackTrace();
            // You might want to set a default icon or handle the error differently
        }
    }

    /**
     * Sets the icon on the provided JButton using an ImageIcon object.
     *
     * @param button    The JButton to set the icon on.
     * @param icon      The ImageIcon object to set.
     */
    public static void setButtonIcon(JButton button, ImageIcon icon) {
        button.setIcon(icon);
        // Optionally, remove the text if you only want to display the icon
        // button.setText("");
    }
}
