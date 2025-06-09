package com.inventory.ui.product;

import com.inventory.dao.ProductsDAOImpl;
import com.inventory.model.Product;
import com.inventory.model.User;
import com.inventory.ui.config.GUIConstants;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.util.stream.Collectors;

public class ProductManagementPanel extends JPanel {

    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField searchField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private boolean isViewOnly;
    private JPanel inputPanel;
    private JPanel searchPanel; // Changed to be a GridBagLayout
    private JLabel searchLabel;
    private JLabel messageLabel; // For displaying success/error messages
    private JComboBox<String> sortComboBox;
    private static final String SORT_BY_PRICE_ASC = "Price (Asc)";
    private static final String SORT_BY_PRICE_DESC = "Price (Desc)";
    private static final String SORT_BY_NAME_ASC = "Name (Asc)";
    private static final String SORT_BY_NAME_DESC = "Name (Desc)";
    private JRadioButton myProductsRadioButton;  // Radio button to filter by current user
    private JRadioButton allProductsRadioButton;
    private ButtonGroup filterButtonGroup;  // Button group to manage radio button selection
    private User user;
    private JPanel bottomPanel;
    private JLabel sortLabel;
    private JLabel filterLabel;

    // Colors for the flat design
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219); // Blue
    private static final Color BUTTON_HOVER_COLOR = new Color(41, 128, 185); // Darker Blue
    private static final Color BORDER_COLOR = new Color(204, 204, 204);
    private static final Color ERROR_COLOR = new Color(231, 76, 60); // Red for errors
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113); // Green for success

    // Fonts
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TEXT_FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font MESSAGE_FONT = new Font("Segoe UI", Font.ITALIC, 12);

    public ProductManagementPanel(boolean viewOnly, User user) {
        this.isViewOnly = viewOnly;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Initialize table and model
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price");
        tableModel.addColumn("User");
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only allow single row selection
        styleTable();

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR); //ScrollPane background
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0)); // Remove border
        add(scrollPane, BorderLayout.CENTER);

        bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        searchLabel = new JLabel("Search:");
        searchLabel.setFont(LABEL_FONT);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(360, 25));
        searchButton = createButton("Enter");
        sortComboBox = new JComboBox<>(new String[]{
            SORT_BY_NAME_ASC,
            SORT_BY_NAME_DESC,
            SORT_BY_PRICE_ASC,
            SORT_BY_PRICE_DESC
        });
        sortComboBox.setSelectedItem(SORT_BY_NAME_ASC); // Default sort order
        styleComboBox(sortComboBox); // Apply custom styling

        // --- Improved searchPanel using GridBagLayout ---
        searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(new EmptyBorder(13, 13, 13, 13)); // Padding
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Radio buttons for filtering
        myProductsRadioButton = new JRadioButton("Your Products");
        myProductsRadioButton.setBackground(BACKGROUND_COLOR);
        myProductsRadioButton.setForeground(TEXT_COLOR);
        myProductsRadioButton.setFont(LABEL_FONT);
        allProductsRadioButton = new JRadioButton("All Products");
        allProductsRadioButton.setBackground(BACKGROUND_COLOR);
        allProductsRadioButton.setForeground(TEXT_COLOR);
        allProductsRadioButton.setFont(LABEL_FONT);
        filterButtonGroup = new ButtonGroup();
        filterButtonGroup.add(myProductsRadioButton);
        filterButtonGroup.add(allProductsRadioButton);
        allProductsRadioButton.setSelected(true); // Default selection

        // Sorting Panel
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sortPanel.setLayout(new GridBagLayout());
        sortPanel.setBackground(BACKGROUND_COLOR);
        sortPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcSort = new GridBagConstraints();
        gbcSort.insets = new Insets(0, 5, 0, 5);
        
        sortLabel = new JLabel("Sort By:");
        gbcSort.gridx = 0;
        gbcSort.gridy = 0;
        gbcSort.anchor = GridBagConstraints.WEST;
        sortLabel.setFont(LABEL_FONT);
        sortPanel.add(sortLabel, gbcSort);
        
        gbcSort.gridx = 0;
        gbcSort.gridy = 1;
        gbcSort.anchor = GridBagConstraints.WEST;
        sortPanel.add(sortComboBox, gbcSort);
        
        filterLabel = new JLabel("Filter:");
        gbcSort.gridx = 1;
        gbcSort.gridy = 0;
        gbcSort.anchor = GridBagConstraints.WEST;
        filterLabel.setFont(LABEL_FONT);
        sortPanel.add(filterLabel, gbcSort);
        
        gbcSort.gridx = 1;
        gbcSort.gridy = 1;
        gbcSort.anchor = GridBagConstraints.WEST;
        sortPanel.add(allProductsRadioButton, gbcSort);
        
        gbcSort.gridx = 2;
        gbcSort.gridy = 1;
        gbcSort.anchor = GridBagConstraints.WEST;
        sortPanel.add(myProductsRadioButton, gbcSort); // Add the radio button here

        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(searchPanel, BorderLayout.WEST);
        bottomPanel.add(sortPanel, BorderLayout.EAST);
        // ------------------------------------------------

        if (!isViewOnly) {
            // Input panel for adding/updating products
            inputPanel = new JPanel(new GridBagLayout());
            inputPanel.setBackground(BACKGROUND_COLOR);
            inputPanel.setBorder(new EmptyBorder(20, 20, 10, 20)); // Reduced bottom padding

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;

            JLabel nameLabel = createLabel("Name:");
            nameField = createTextField();
            JLabel categoryLabel = createLabel("Category:");
            categoryField = createTextField();
            JLabel priceLabel = createLabel("Price:");
            priceField = createTextField();
            addButton = createButton("Add");
            updateButton = createButton("Update");
            deleteButton = createButton("Delete");

            // Add labels and fields using GridBagConstraints
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_END;
            inputPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            inputPanel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_END;
            inputPanel.add(categoryLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            inputPanel.add(categoryField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.LINE_END;
            inputPanel.add(priceLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            inputPanel.add(priceField, gbc);

            // Buttons
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); //use a panel for buttons
            buttonPanel.setBackground(BACKGROUND_COLOR);
            buttonPanel.add(addButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);
            inputPanel.add(buttonPanel, gbc);

            // Message Label
            messageLabel = createMessageLabel();
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.CENTER;
            inputPanel.add(messageLabel, gbc);

            add(inputPanel, BorderLayout.NORTH); // Add input panel to the main panel

            // Action listeners for buttons
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addProduct(user);
                }
            });

            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateProduct(user);
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteProduct();
                }
            });

            // Add a MouseListener to the table to automatically populate the input fields when a row is selected
            productTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int selectedRow = productTable.getSelectedRow();
                    if (selectedRow != -1) {
                        nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                        categoryField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                        priceField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    }
                }
            });
        } else {
            add(searchPanel, BorderLayout.NORTH);
        }

        // Action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProducts(searchField.getText());
            }
        });

        // Listener for the search field to allow searching on Enter key press
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProducts(searchField.getText());
            }
        });

        // Add listener to the sort combo box
        sortComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortProducts();
            }
        });

        // Add listener to the "My Products" radio button
        myProductsRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProducts(user); // Reload products based on the filter
            }
        });

        // Add listener for the All Products radio button.
        allProductsRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProducts(user);
            }
        });

        loadProducts(user);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(LABEL_FONT);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBorder(new RoundedBorder(5)); // Use the custom rounded border
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_COLOR);
        textField.setFont(TEXT_FIELD_FONT);
        return textField;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });
        return button;
    }

    private JLabel createMessageLabel() {
        JLabel label = new JLabel("");
        label.setFont(MESSAGE_FONT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void styleTable() {
        productTable.setBackground(BACKGROUND_COLOR);
        productTable.setForeground(TEXT_COLOR);
        productTable.setGridColor(BORDER_COLOR);
        productTable.setRowHeight(30);
        productTable.getTableHeader().setBackground(BACKGROUND_COLOR);
        productTable.getTableHeader().setForeground(TEXT_COLOR);
        productTable.setSelectionBackground(ACCENT_COLOR);
        productTable.setSelectionForeground(Color.WHITE);
        productTable.setFont(TEXT_FIELD_FONT); // Use the same font as text fields

        // Center align the text in the columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setFont(TEXT_FIELD_FONT);
        comboBox.setBorder(new RoundedBorder(5)); // Use the rounded border
        // Customize renderer to change dropdown appearance
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(ACCENT_COLOR);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(TEXT_COLOR);
                }
                setFont(TEXT_FIELD_FONT);
                return this;
            }
        });
    }

    private void loadProducts(User user) {
        try {
            ProductsDAOImpl productsDAO = new ProductsDAOImpl();
            List<Product> products = productsDAO.getAllProducts();
            if (myProductsRadioButton.isSelected()) {
                products = products.stream()
                        .filter(product -> product.getUserID() == user.getUserID())
                        .collect(Collectors.toList());
            }
            tableModel.setRowCount(0); // Clear the table
            for (Product product : products) {
                tableModel.addRow(new Object[]{
                    product.getProductID(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getUserID() // Display the User ID
                });
            }
            if (products.isEmpty()) {
                messageLabel.setForeground(TEXT_COLOR);
                messageLabel.setText("No products found.");
            } else {
                messageLabel.setText(""); // Clear any previous message
            }
        } catch (SQLException e) {
            messageLabel.setForeground(ERROR_COLOR);
            messageLabel.setText("Error loading products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addProduct(User currentUser) {
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String priceStr = priceField.getText().trim();

        if (name.isEmpty() || category.isEmpty() || priceStr.isEmpty()) {
            messageLabel.setForeground(ERROR_COLOR);
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            Product newProduct = new Product();
            newProduct.setName(name);
            newProduct.setCategory(category);
            newProduct.setPrice(price);

            ProductsDAOImpl productsDAO = new ProductsDAOImpl();
            productsDAO.addProduct(newProduct, currentUser.getUserID()); // Pass the user ID
            messageLabel.setForeground(SUCCESS_COLOR);
            messageLabel.setText("Product added successfully.");
            clearInputFields();
            loadProducts(currentUser); // Reload the table to display the new product
        } catch (NumberFormatException ex) {
            messageLabel.setForeground(ERROR_COLOR);
            messageLabel.setText("Invalid price format.");
        } catch (SQLException ex) {
            messageLabel.setForeground(ERROR_COLOR);
            // messageLabel.setText("Error adding product: " + ex.getMessage(), false);
            ex.printStackTrace(); // Good practice for debugging
        }
    }

    private void updateProduct(User user) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Please select a product to update.", false);
            return;
        }

        int productID = (int) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String category = categoryField.getText();
        String priceStr = priceField.getText();

        if (name.isEmpty() || category.isEmpty() || priceStr.isEmpty()) {
            showMessage("Please fill in all fields.", false);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int userID = user.getUserID();
            Product product = new Product(productID, name, category, price, userID);
            ProductsDAOImpl productDAO = new ProductsDAOImpl();
            productDAO.updateProduct(product);
            loadProducts(user);
            clearInputFields();
            showMessage("Product updated successfully.", true);
        } catch (NumberFormatException e) {
            showMessage("Invalid price format.", false);
        } catch (SQLException e) {
            showMessage("Error updating product: " + e.getMessage(), false);
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Please select a product to delete.", false);
            return;
        }
        int productID = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            ProductsDAOImpl productDAO = new ProductsDAOImpl();
            productDAO.deleteProduct(productID);
            loadProducts(null); // Pass null user here
            clearInputFields();
            showMessage("Product deleted successfully", true);

        } catch (SQLException e) {
            showMessage("Error deleting product: " + e.getMessage(), false);
        }

    }

    private void clearInputFields() {
        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message);
        messageLabel.setForeground(success ? SUCCESS_COLOR : ERROR_COLOR);

        // Clear the message after a few seconds
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageLabel.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Custom rounded border for text fields
    private static class RoundedBorder extends AbstractBorder {

        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(BORDER_COLOR);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius / 2, this.radius / 2, this.radius / 2, this.radius / 2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = this.radius / 2;
            return insets;
        }
    }

    private void searchProducts(String searchTerm) {
        try {
            ProductsDAOImpl productDAO = new ProductsDAOImpl();
            List<Product> products = productDAO.searchProducts(searchTerm);
            if (myProductsRadioButton.isSelected()) {
                products = products.stream()
                        .filter(product -> product.getUserID() == user.getUserID())
                        .collect(Collectors.toList());
            }
            tableModel.setRowCount(0); // Clear the table
            for (Product product : products) {
                tableModel.addRow(new Object[]{
                    product.getProductID(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getUserID()
                });
            }
            sortProducts();
        } catch (SQLException | NullPointerException e) {
            showMessage("Error searching products: " + e.getMessage(), false);
        }
    }
    
    private void filterPrices(double searchTerm) {
        try {
            ProductsDAOImpl productDAO = new ProductsDAOImpl();
            List<Product> products = productDAO.filterPrices(searchTerm);
            tableModel.setRowCount(0);
            for (Product product : products) {
                tableModel.addRow(new Object[] {
                    product.getProductID(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getUserID()
                });
            }
            sortProducts();
        } catch (SQLException e) {
            showMessage("Error searching products: " + e.getMessage(), false);
        }
    }

    private void sortProducts() {
        String selectedSort = (String) sortComboBox.getSelectedItem();
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Product product = new Product(
                    (int) tableModel.getValueAt(i, 0), // product ID
                    (String) tableModel.getValueAt(i, 1), // name of the product
                    (String) tableModel.getValueAt(i, 2), // name of category
                    (double) tableModel.getValueAt(i, 3), // price
                    (int) tableModel.getValueAt(i, 4) // user ID
            );
            products.add(product);
        }

        switch (selectedSort) {
            case SORT_BY_PRICE_ASC:
                Collections.sort(products, Comparator.comparingDouble(Product::getPrice));
                break;
            case SORT_BY_PRICE_DESC:
                Collections.sort(products, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                break;
            case SORT_BY_NAME_ASC:
                Collections.sort(products, Comparator.comparing(Product::getName));
                break;
            case SORT_BY_NAME_DESC:
                Collections.sort(products, (p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
                break;
        }
        tableModel.setRowCount(0);
        for (Product product : products) {
            tableModel.addRow(new Object[]{product.getProductID(), product.getName(), product.getCategory(), product.getPrice(), product.getUserID()});
        }
    }
}