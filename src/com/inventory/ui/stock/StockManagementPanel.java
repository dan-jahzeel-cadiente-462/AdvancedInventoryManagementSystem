package com.inventory.ui.stock;

import com.inventory.dao.ProductsDAO;
import com.inventory.dao.ProductsDAOImpl;
import com.inventory.dao.StockDAOImpl;
import com.inventory.dao.SuppliersDAO;
import com.inventory.dao.SuppliersDAOImpl;
import com.inventory.model.Product;
import com.inventory.model.Stock;
import com.inventory.model.Supplier;
import com.inventory.ui.details.ProductDetails;
import com.inventory.ui.details.SupplierDetails;
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
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional; // Import Optional

public class StockManagementPanel extends JPanel {

    private JTable stockTable;
    private DefaultTableModel tableModel;
    private JComboBox<Product> productComboBox;
    private JComboBox<Supplier> supplierComboBox;
    private JTextField quantityField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton viewProductInfo;
    private JButton viewSupplierInfo;
    private JButton refreshButton;
    private boolean isViewOnly;
    private JPanel inputPanel;
    private JLabel messageLabel; // Declare messageLabel here
    private Map<Integer, Product> productMap;
    private Map<Integer, Supplier> supplierMap;
    private StockDAOImpl stockDAO;

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

    public StockManagementPanel(boolean viewOnly) {
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
        tableModel.addColumn("Stock ID");
        tableModel.addColumn("Product");
        tableModel.addColumn("Supplier");
        tableModel.addColumn("Quantity Added");
        tableModel.addColumn("Date Added");
        stockTable = new JTable(tableModel);
        stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(stockTable);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(scrollPane, BorderLayout.CENTER);

        productMap = new HashMap<>();
        supplierMap = new HashMap<>();
        stockDAO = new StockDAOImpl(); //initialize the stockDAO

        // Initialize messageLabel here
        messageLabel = createMessageLabel();

        if (!isViewOnly) {
            // Input panel for adding/updating stock
            inputPanel = new JPanel(new GridBagLayout());
            inputPanel.setBackground(BACKGROUND_COLOR);
            inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.gridwidth = 1;

            JLabel productLabel = createLabel("Product:");
            productComboBox = createProductComboBox();
            JLabel supplierLabel = createLabel("Supplier:");
            supplierComboBox = createSupplierComboBox();
            JLabel quantityLabel = createLabel("Quantity:");
            quantityField = createTextField();
            addButton = createButton("Add");
            updateButton = createButton("Update");
            deleteButton = createButton("Delete");
            viewProductInfo = createButton("Product Info");
            viewSupplierInfo = createButton("Supplier Info");
            refreshButton = createButton("Refresh");

            // Populate combo boxes
            populateComboBoxes();

            // Add components to the input panel
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_END;
            inputPanel.add(productLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            inputPanel.add(productComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_END;
            inputPanel.add(supplierLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            inputPanel.add(supplierComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.LINE_END;
            inputPanel.add(quantityLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            inputPanel.add(quantityField, gbc);

            // Button panel
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(BACKGROUND_COLOR);
            buttonPanel.add(addButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(viewProductInfo);
            buttonPanel.add(viewSupplierInfo);
            buttonPanel.add(refreshButton);
            inputPanel.add(buttonPanel, gbc);

            // Message label
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.CENTER;
            inputPanel.add(messageLabel, gbc);

            add(inputPanel, BorderLayout.NORTH);

            // Action listeners
            addButton.addActionListener(e -> addStock());
            updateButton.addActionListener(e -> updateStock());
            deleteButton.addActionListener(e -> deleteStock());
            viewProductInfo.addActionListener(e -> displayProductInfo());
            viewSupplierInfo.addActionListener(e -> displaySupplierInfo());
            refreshButton.addActionListener(e -> {
                populateComboBoxes();
                loadStock();
            });

            // Table listener
            stockTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleTableClick();
                }
            });
        }
        loadStock();
    }

    private void handleTableClick() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow != -1) {
            String productName = tableModel.getValueAt(selectedRow, 1).toString();
            String supplierName = tableModel.getValueAt(selectedRow, 2).toString();
            String quantity = tableModel.getValueAt(selectedRow, 3).toString();

            // Use Optional to handle potential nulls and simplify logic
            Optional<Product> selectedProd = productMap.values().stream()
                    .filter(product -> product.getName().equals(productName))
                    .findFirst();

            Optional<Supplier> selectedSupp = supplierMap.values().stream()
                    .filter(supplier -> supplier.getName().equals(supplierName))
                    .findFirst();

            selectedProd.ifPresent(productComboBox::setSelectedItem);
            selectedSupp.ifPresent(supplierComboBox::setSelectedItem);
            quantityField.setText(quantity);
        }
    }

    private void displayProductInfo() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) {
            showMessage("No product selected.", ERROR_COLOR);
            return;
        }
        try {
            new ProductDetails(selectedProduct).setVisible(true);
        } catch (Exception e) {
            showMessage("Error displaying product details: " + e.getMessage(), ERROR_COLOR);
        }
    }

    private void displaySupplierInfo() {
        Supplier selectedSupplier = (Supplier) supplierComboBox.getSelectedItem();
        if (selectedSupplier == null) {
            showMessage("No supplier selected.", ERROR_COLOR);
            return;
        }
        try {
            new SupplierDetails(selectedSupplier).setVisible(true);
        } catch (Exception e) {
            showMessage("Error displaying supplier details: " + e.getMessage(), ERROR_COLOR);
        }
    }

    private JComboBox<Product> createProductComboBox() {
        JComboBox<Product> comboBox = new JComboBox<>();
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setFont(TEXT_FIELD_FONT);
        return comboBox;
    }

    private JComboBox<Supplier> createSupplierComboBox() {
        JComboBox<Supplier> comboBox = new JComboBox<>();
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setFont(TEXT_FIELD_FONT);
        return comboBox;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(LABEL_FONT);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBorder(new RoundedBorder(5));
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
        stockTable.setBackground(BACKGROUND_COLOR);
        stockTable.setForeground(TEXT_COLOR);
        stockTable.setGridColor(BORDER_COLOR);
        stockTable.setRowHeight(30);
        stockTable.getTableHeader().setBackground(BACKGROUND_COLOR);
        stockTable.getTableHeader().setForeground(TEXT_COLOR);
        stockTable.setSelectionBackground(ACCENT_COLOR);
        stockTable.setSelectionForeground(Color.WHITE);
        stockTable.setFont(TEXT_FIELD_FONT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < stockTable.getColumnCount(); i++) {
            stockTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void populateComboBoxes() {
        try {
            ProductsDAO productDAO = new ProductsDAOImpl();
            List<Product> products = productDAO.getAllProducts();
            productComboBox.removeAllItems();
            productMap.clear();
            products.forEach(product -> {
                productComboBox.addItem(product);
                productMap.put(product.getProductID(), product);
            });

            SuppliersDAOImpl supplierDAO = new SuppliersDAOImpl();
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            supplierComboBox.removeAllItems();
            supplierMap.clear();
            suppliers.forEach(supplier -> {
                supplierComboBox.addItem(supplier);
                supplierMap.put(supplier.getSupplierID(), supplier);
            });
            showMessage("ComboBoxes Populated", SUCCESS_COLOR);
        } catch (SQLException e) {
            showMessage("Error populating combo boxes: " + e.getMessage(), ERROR_COLOR);
        }
    }

    private void loadStock() {
        try {
            List<Stock> stocks = stockDAO.getAllStock();
            tableModel.setRowCount(0);
            stocks.forEach(stock -> {
                String productName = Optional.ofNullable(productMap.get(stock.getProductID()))
                        .map(Product::getName)
                        .orElse("N/A");
                String supplierName = Optional.ofNullable(supplierMap.get(stock.getSupplierID()))
                        .map(Supplier::getName)
                        .orElse("N/A");

                tableModel.addRow(new Object[]{
                        stock.getStockID(),
                        productName,
                        supplierName,
                        stock.getQuantityAdded(),
                        stock.getDateAdded()
                });
            });
            showMessage("Stock Loaded", SUCCESS_COLOR);
        } catch (SQLException e) {
            showMessage("Error loading stock: " + e.getMessage(), ERROR_COLOR);
        }
    }

    private void addStock() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        Supplier selectedSupplier = (Supplier) supplierComboBox.getSelectedItem();
        String quantityStr = quantityField.getText();

        if (selectedProduct == null || selectedSupplier == null || quantityStr.isEmpty()) {
            showMessage("Please fill in all fields.", ERROR_COLOR);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                showMessage("Quantity must be greater than zero.", ERROR_COLOR);
                return;
            }

            // Check if the stock entry already exists for the same product and supplier.
            Stock existingStock = stockDAO.getStockByProductAndSupplier(selectedProduct.getProductID(), selectedSupplier.getSupplierID());

            if (existingStock != null) {
                // Update the existing stock
                int newQuantity = existingStock.getQuantityAdded() + quantity;
                Stock updatedStock = new Stock(existingStock.getStockID(), selectedProduct.getProductID(), selectedSupplier.getSupplierID(), newQuantity, new Date(System.currentTimeMillis()));
                stockDAO.updateStock(updatedStock);
                showMessage("Stock quantity updated successfully.", SUCCESS_COLOR);
            } else {
                // Add new stock.
                Stock stock = new Stock(0, selectedProduct.getProductID(), selectedSupplier.getSupplierID(), quantity, new Date(System.currentTimeMillis()));
                stockDAO.addStock(stock);
                showMessage("Stock added successfully.", SUCCESS_COLOR);
            }
            loadStock();
            clearInputFields();
        } catch (NumberFormatException e) {
            showMessage("Invalid quantity format.", ERROR_COLOR);
        } catch (SQLException e) {
            showMessage("Error adding/updating stock: " + e.getMessage(), ERROR_COLOR);
        }
    }

    private void updateStock() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Please select a stock entry to update.", ERROR_COLOR);
            return;
        }

        int stockID = (int) tableModel.getValueAt(selectedRow, 0);
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        Supplier selectedSupplier = (Supplier) supplierComboBox.getSelectedItem();
        String quantityStr = quantityField.getText();

        if (selectedProduct == null || selectedSupplier == null || quantityStr.isEmpty()) {
            showMessage("Please fill in all fields.", ERROR_COLOR);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                showMessage("Quantity must be greater than zero.", ERROR_COLOR);
                return;
            }
            Stock stock = new Stock(stockID, selectedProduct.getProductID(), selectedSupplier.getSupplierID(), quantity, new Date(System.currentTimeMillis()));
            stockDAO.updateStock(stock);
            loadStock();
            clearInputFields();
            showMessage("Stock updated successfully.", SUCCESS_COLOR);
        } catch (NumberFormatException e) {
            showMessage("Invalid quantity format.", ERROR_COLOR);
        } catch (SQLException e) {
            showMessage("Error updating stock: " + e.getMessage(), ERROR_COLOR);
        }
    }

    private void deleteStock() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Please select a stock entry to delete.", ERROR_COLOR);
            return;
        }

        int stockID = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            stockDAO.deleteStock(stockID);
            loadStock();
            clearInputFields();
            showMessage("Stock deleted successfully.", SUCCESS_COLOR);
        } catch (SQLException e) {
            showMessage("Error deleting stock: " + e.getMessage(), ERROR_COLOR);
        }
    }

    private void clearInputFields() {
        productComboBox.setSelectedIndex(0);
        supplierComboBox.setSelectedIndex(0);
        quantityField.setText("");
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

    private void showMessage(String message, Color color) {
        messageLabel.setForeground(color);
        messageLabel.setText(message);
        // Clear the message after a few seconds
        Timer timer = new Timer(5000, e -> messageLabel.setText(""));
        timer.setRepeats(false);
        timer.start();
    }
}

