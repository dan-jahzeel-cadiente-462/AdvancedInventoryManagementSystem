// ui/details/ProductDetails.java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.ui.details;

import com.inventory.model.Product;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author USER
 */
public class ProductDetails extends Details {
    public ProductDetails(Product product) {
        setTitle("Product Info - " + product.getName());
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
//        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        addComponentToPanel(0, 0, gbc, new JLabel("Product ID:"), detailsPanel);
        addComponentToPanel(1, 0, gbc, new JLabel(String.valueOf(product.getProductID())), detailsPanel);

        addComponentToPanel(0, 1, gbc, new JLabel("Name:"), detailsPanel);
        addComponentToPanel(1, 1, gbc, new JLabel(product.getName()), detailsPanel);

        addComponentToPanel(0, 2, gbc, new JLabel("Category:"), detailsPanel);
        addComponentToPanel(1, 2, gbc, new JLabel(product.getCategory()), detailsPanel);

        addComponentToPanel(0, 3, gbc, new JLabel("Price:"), detailsPanel);
        addComponentToPanel(1, 3, gbc, new JLabel(String.valueOf(product.getPrice())), detailsPanel);

        add(detailsPanel);
        pack(); // Adjust the frame size to fit the components
        setLocationRelativeTo(null); // Center the frame
    }
}