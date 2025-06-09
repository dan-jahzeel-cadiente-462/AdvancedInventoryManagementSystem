/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.ui.details;

import com.inventory.model.Supplier;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author USER
 */
public class SupplierDetails extends Details {
    private JPanel info;
    private JLabel idLabel, nameLabel, contactInfoLabel;
    
    public SupplierDetails(Supplier supplier) {
        setTitle("Product Info - " + supplier.getName());
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        addComponentToPanel(0, 0, gbc, new JLabel("Product ID:"), detailsPanel);
        addComponentToPanel(1, 0, gbc, new JLabel(String.valueOf(supplier.getSupplierID())), detailsPanel);

        addComponentToPanel(0, 1, gbc, new JLabel("Name:"), detailsPanel);
        addComponentToPanel(1, 1, gbc, new JLabel(supplier.getName()), detailsPanel);

        addComponentToPanel(0, 2, gbc, new JLabel("Category:"), detailsPanel);
        addComponentToPanel(1, 2, gbc, new JLabel(supplier.getContactInfo()), detailsPanel);

        add(detailsPanel);
        pack(); // Adjust the frame size to fit the components
        setLocationRelativeTo(null); // Center the frame
    }
}
