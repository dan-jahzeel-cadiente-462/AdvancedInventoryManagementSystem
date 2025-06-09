package com.inventory.ui.sale;

import com.inventory.dao.ProductsDAO;
import com.inventory.dao.ProductsDAOImpl;
import com.inventory.dao.SalesDAOImpl;
import com.inventory.dao.StockDAOImpl;
import com.inventory.model.Product;
import com.inventory.model.Sale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class SalesManagementPanel extends JPanel {

    private JTable salesTable;
    private DefaultTableModel tableModel;
    private JComboBox<Product> productComboBox;
    private JTextField quantityField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private boolean isViewOnly;
    private JPanel inputPanel;
    private Map<Integer, Product> productMap;
    private ProductsDAOImpl productsDAO;
    private SalesDAOImpl salesDAO;
    private StockDAOImpl stockDAO;
    private Map<Integer, Integer> productStockMap;
    // Search and Sort
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> sortComboBox;
    private JPanel searchPanel;
    private JPanel bottomPanel;
    private JLabel searchLabel;
    private JLabel sortLabel;
    private JButton refreshButton; // Added refresh button
    
    // Filter
    // private JPanel filterPanel;
    private JLabel filterLabel;
    private JRadioButton mySalesRadioButton;
    private JRadioButton allSalesRadioButton;
    private ButtonGroup filterButtonGroup;
    
    private static final String SORT_BY_QUANTITY_ASC = "Quantity (Asc)";
    private static final String SORT_BY_QUANTITY_DESC = "Quantity (Desc)";
    private static final String SORT_BY_DATE_ASC = "Date (Asc)";
    private static final String SORT_BY_DATE_DESC = "Date (Desc)";
    // Constants for column names
    private static final String SALE_ID_COLUMN = "Sale ID";
    private static final String PRODUCT_COLUMN = "Product";
    private static final String QUANTITY_SOLD_COLUMN = "Quantity Sold";
    private static final String SALE_DATE_COLUMN = "Sale Date";
    private static final String TOTAL_AMOUNT_COLUMN = "Total Amount";
    private static final String PRICE_COLUMN = "Price";
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
    private JLabel messageLabel;

    public SalesManagementPanel(boolean viewOnly) {
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
        tableModel.addColumn(SALE_ID_COLUMN);
        tableModel.addColumn(PRODUCT_COLUMN);
        tableModel.addColumn(QUANTITY_SOLD_COLUMN);
        tableModel.addColumn(SALE_DATE_COLUMN);
        tableModel.addColumn(TOTAL_AMOUNT_COLUMN);
        tableModel.addColumn(PRICE_COLUMN);
        salesTable = new JTable(tableModel);
        salesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable();
        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR); //ScrollPane background
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0)); // Remove border
        add(scrollPane, BorderLayout.CENTER);
        productMap = new HashMap<>();
        productsDAO = new ProductsDAOImpl();
        salesDAO = new SalesDAOImpl();
        stockDAO = new StockDAOImpl();
        productStockMap = new HashMap<>();
        // Search and Sort
        bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        searchLabel = new JLabel("Search:");
        searchLabel.setFont(LABEL_FONT);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(360, 25));
        searchButton = createButton("Enter");
        sortComboBox = new JComboBox<>(new String[]{
            SORT_BY_QUANTITY_ASC,
            SORT_BY_QUANTITY_DESC,
            SORT_BY_DATE_ASC,
            SORT_BY_DATE_DESC
        });
        sortComboBox.setSelectedItem(SORT_BY_DATE_ASC);
        styleComboBox(sortComboBox);
        searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(new EmptyBorder(13, 13, 13, 13));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
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
        gbcSort.gridx = 1;
        gbcSort.gridy = 0;
        gbcSort.anchor = GridBagConstraints.WEST;
        sortPanel.add(sortComboBox, gbcSort);
        //refresh button
        refreshButton = createButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSales();
                loadProducts();
                clearInputFields();
            }
        });
        
        // filter components
        // filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filterLabel = new JLabel("Filters:");
        filterLabel.setFont(LABEL_FONT);
        mySalesRadioButton = new JRadioButton("Your Sales");
        allSalesRadioButton = new JRadioButton("All Sales");
        mySalesRadioButton.setFont(LABEL_FONT);
        allSalesRadioButton.setFont(LABEL_FONT);
        filterButtonGroup = new ButtonGroup();
        filterButtonGroup.add(mySalesRadioButton);
        filterButtonGroup.add(allSalesRadioButton);
        mySalesRadioButton.setSelected(true);
        sortPanel.add(filterLabel);
        sortPanel.add(mySalesRadioButton);
        sortPanel.add(allSalesRadioButton);
        
        searchPanel.add(refreshButton);
        bottomPanel.add(searchPanel, BorderLayout.NORTH);
        bottomPanel.add(sortPanel, BorderLayout.SOUTH);
        // bottomPanel.add(filterPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
        messageLabel = createMessageLabel();
        add(messageLabel, BorderLayout.NORTH);
        if (!isViewOnly) {
            inputPanel = new JPanel(new GridBagLayout());
            inputPanel.setBackground(BACKGROUND_COLOR);
            inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            JLabel productLabel = new JLabel("Product:");
            productLabel.setFont(LABEL_FONT);
            productComboBox = new JComboBox<>();
            styleComboBox(productComboBox);
            JLabel quantityLabel = new JLabel("Quantity:");
            quantityLabel.setFont(LABEL_FONT);
            quantityField = new JTextField();
            quantityField.setFont(TEXT_FIELD_FONT);
            quantityField.setBorder(new RoundedBorder(5));
            addButton = createButton("Add");
            updateButton = createButton("Update");
            deleteButton = createButton("Delete");
            gbc.gridx = 0;
            gbc.gridy = 0;
            inputPanel.add(productLabel, gbc);
            gbc.gridx = 1;
            gbc.gridy = 0;
            inputPanel.add(productComboBox, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            inputPanel.add(quantityLabel, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
            inputPanel.add(quantityField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(BACKGROUND_COLOR);
            buttonPanel.add(addButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);
            inputPanel.add(buttonPanel, gbc);
            add(inputPanel, BorderLayout.NORTH);
            loadProducts();
        } else {
            // Display only the table
        }
        loadSales();
        // Action Listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSale();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSale();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSale();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchSales();
            }
        });
        sortComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // sortSales();
                filterSales();
            }
        });
        salesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTableClick();
            }
        });
        
        ActionListener filterActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterSales();
            }
        };
        mySalesRadioButton.addActionListener(filterActionListener);
        allSalesRadioButton.addActionListener(filterActionListener);
    }

    private void handleTableClick() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow != -1) {
            int saleId = (int) tableModel.getValueAt(selectedRow, 0);
            Product selectedProduct = null;
            for (Map.Entry<Integer, Product> entry : productMap.entrySet()) {
                if (entry.getValue().getName().equals(tableModel.getValueAt(selectedRow, 1))) {
                    selectedProduct = entry.getValue();
                    break;
                }
            }
            productComboBox.setSelectedItem(selectedProduct);
            quantityField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        }
    }

    private void loadProducts() {
        try {
            List<Product> products = productsDAO.getAllProducts();
            productComboBox.removeAllItems();
            productMap.clear();
            productStockMap.clear();
            for (Product product : products) {
                productComboBox.addItem(product);
                productMap.put(product.getProductID(), product);
                int stock = stockDAO.getProductStock(product.getProductID());
                productStockMap.put(product.getProductID(), stock);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadSales() {
        try {
            List<Sale> sales = salesDAO.getAllSales();
            if (sales.isEmpty()) {
                showMessage("No sales to display.", TEXT_COLOR);
            } else {
                showMessage("Sales loaded successfully", SUCCESS_COLOR);
            }
            //  **CALL filterSales() INSTEAD OF displaySales()**
            filterSales();
        } catch (SQLException e) {
            showMessage("Error loading sales: " + e.getMessage(), ERROR_COLOR);
            e.printStackTrace();
        }
    }

    private void displaySales(List<Sale> sales) {
        tableModel.setRowCount(0);
        for (Sale sale : sales) {
            Product product = productMap.get(sale.getProductID());
            if (product != null) {
                Object[] rowData = {
                    sale.getSaleID(),
                    product.getName(),
                    sale.getQuantitySold(),
                    sale.getSaleDate(),
                    sale.getTotalAmount(),
                    product.getPrice()
                };
                tableModel.addRow(rowData);
            } else {
                Object[] rowData = {
                    sale.getSaleID(),
                    "N/A",
                    sale.getQuantitySold(),
                    sale.getSaleDate(),
                    sale.getTotalAmount(),
                    "N/A"
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    private void filterSales() {
        try {
            List<Sale> allSales = salesDAO.getAllSales();
            List<Sale> filteredSales = new ArrayList<>();

            if (mySalesRadioButton.isSelected()) {
                //  **TODO:** Replace this with actual "user's sales" logic
                for (Sale sale : allSales) {
                    if (sale.getSaleID() % 2 == 0) {
                        filteredSales.add(sale);
                    }
                }
            } else {
                filteredSales.addAll(allSales);
            }

            //  **SORT the filtered list:**
            String selectedSort = (String) sortComboBox.getSelectedItem();
            switch (selectedSort) {
                case SORT_BY_QUANTITY_ASC:
                    Collections.sort(filteredSales, Comparator.comparingInt(Sale::getQuantitySold));
                    break;
                case SORT_BY_QUANTITY_DESC:
                    Collections.sort(filteredSales, (s1, s2) -> Integer.compare(s2.getQuantitySold(), s1.getQuantitySold()));
                    break;
                case SORT_BY_DATE_ASC:
                    Collections.sort(filteredSales, Comparator.comparing(Sale::getSaleDate));
                    break;
                case SORT_BY_DATE_DESC:
                    Collections.sort(filteredSales, (s1, s2) -> s2.getSaleDate().compareTo(s1.getSaleDate()));
                    break;
            }

            displaySales(filteredSales);  // Display the sorted, filtered list

        } catch (SQLException e) {
            showMessage("Error filtering sales: " + e.getMessage(), ERROR_COLOR);
            e.printStackTrace();
        }
    }


    private void addSale() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        String quantityStr = quantityField.getText();
        if (selectedProduct == null || quantityStr.isEmpty()) {
            showMessage("Please select a product and enter quantity.", ERROR_COLOR);
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                showMessage("Quantity must be greater than zero.", ERROR_COLOR);
                return;
            }
            // Check if there is enough stock
            int currentStock = productStockMap.get(selectedProduct.getProductID());
            if (quantity > currentStock) {
                showMessage("Insufficient stock. Available stock: " + currentStock, ERROR_COLOR);
                return;
            }
            double totalAmount = quantity * selectedProduct.getPrice();
            Sale sale = new Sale(0, selectedProduct.getProductID(), quantity, new Date(), totalAmount);
            salesDAO.addSale(sale);
            // Update stock quantity
            stockDAO.updateStockQuantity(selectedProduct.getProductID(), currentStock - quantity);
            loadSales();
            loadProducts(); //update product list.
            clearInputFields();
            showMessage("Sale added successfully.", SUCCESS_COLOR);
        } catch (NumberFormatException e) {
            showMessage("Invalid quantity format.", ERROR_COLOR);
        } catch (SQLException e) {
            showMessage("Error adding sale: " + e.getMessage(), ERROR_COLOR);
            e.printStackTrace();
        }
    }

    private void updateSale() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Please select a sale to update.", ERROR_COLOR);
            return;
        }
        int saleID = (int) tableModel.getValueAt(selectedRow, 0);
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        String quantityStr = quantityField.getText();
        if (selectedProduct == null || quantityStr.isEmpty()) {
            showMessage("Please select a product and enter quantity.", ERROR_COLOR);
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                showMessage("Quantity must be greater than zero.", ERROR_COLOR);
                return;
            }
            // Get the old quantity from the table
            int oldQuantity = (int) tableModel.getValueAt(selectedRow, 2);
            int productID = selectedProduct.getProductID();
            // Check for sufficient stock.
            int currentStock = productStockMap.get(productID);
            if (quantity > currentStock + oldQuantity) {
                showMessage("Insufficient stock. Available stock: " + (currentStock + oldQuantity), ERROR_COLOR);
                return;
            }
            double totalAmount = quantity * selectedProduct.getPrice();
            Sale sale = new Sale(saleID, selectedProduct.getProductID(), quantity, (Date) tableModel.getValueAt(selectedRow, 3), totalAmount); //keep original date.
            salesDAO.updateSale(sale);
            // Update Stock
            stockDAO.updateStockQuantity(productID, currentStock + oldQuantity - quantity);
            loadSales();
            loadProducts();
            clearInputFields();
            showMessage("Sale updated successfully.", SUCCESS_COLOR);
        } catch (NumberFormatException e) {
            showMessage("Invalid quantity format.", ERROR_COLOR);
        } catch (SQLException e) {
            showMessage("Error updating sale: " + e.getMessage(), ERROR_COLOR);
            e.printStackTrace();
        }
    }

    private void deleteSale() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Please select a sale entry to delete.", ERROR_COLOR);
            return;
        }
        int saleID = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            // Get the quantity of the sale to be deleted.
            int quantity = (int) tableModel.getValueAt(selectedRow, 2);
            Product selectedProduct = null;
            for (Map.Entry<Integer, Product> entry : productMap.entrySet()) {
                if (entry.getValue().getName().equals(tableModel.getValueAt(selectedRow, 1))) {
                    selectedProduct = entry.getValue();
                    break;
                }
            }
            int productID = selectedProduct.getProductID();
            //get current stock
            int currentStock = productStockMap.get(productID);
            //increase stock.
            stockDAO.updateStockQuantity(productID, currentStock + quantity);
            salesDAO.deleteSale(saleID);
            loadSales();
            loadProducts(); //update products.
            clearInputFields();
            showMessage("Sale deleted successfully.", SUCCESS_COLOR);
        } catch (SQLException e) {
            showMessage("Error deleting sale: " + e.getMessage(), ERROR_COLOR);
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        productComboBox.setSelectedIndex(0);
        quantityField.setText("");
    }

    private void searchSales() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadSales();
            return;
        }
        try {
            List<Sale> allSales = salesDAO.getAllSales();
            List<Sale> filteredSales = new ArrayList<>();
            for (Sale sale : allSales) {
                Product product = productMap.get(sale.getProductID());
                if (product != null
                        && (String.valueOf(sale.getSaleID()).contains(searchTerm)
                        || product.getName().toLowerCase().contains(searchTerm.toLowerCase())
                        || String.valueOf(sale.getQuantitySold()).contains(searchTerm)
                        || sale.getSaleDate().toString().contains(searchTerm)
                        || String.valueOf(sale.getTotalAmount()).contains(searchTerm))) {
                    filteredSales.add(sale);
                }
            }
            if (filteredSales.isEmpty()) {
                showMessage("No matching sales found.", TEXT_COLOR);
            } else {
                showMessage("Matching sales found.", SUCCESS_COLOR);
            }
            displaySales(filteredSales);
        } catch (SQLException e) {
            showMessage("Error searching sales: " + e.getMessage(), ERROR_COLOR);
            e.printStackTrace();
        }
    }

    private void sortSales() {
        List<Sale> sales;
        try {
            sales = salesDAO.getAllSales();
        } catch (SQLException e) {
            showMessage("Error loading sales for sorting: " + e.getMessage(), ERROR_COLOR);
            e.printStackTrace();
            return;
        }
        String selectedSort = (String) sortComboBox.getSelectedItem();
        switch (selectedSort) {
            case SORT_BY_QUANTITY_ASC:
                Collections.sort(sales, Comparator.comparingInt(Sale::getQuantitySold));
                break;
            case SORT_BY_QUANTITY_DESC:
                Collections.sort(sales, (s1, s2) -> Integer.compare(s2.getQuantitySold(), s1.getQuantitySold()));
                break;
            case SORT_BY_DATE_ASC:
                Collections.sort(sales, Comparator.comparing(Sale::getSaleDate));
                break;
            case SORT_BY_DATE_DESC:
                Collections.sort(sales, (s1, s2) -> s2.getSaleDate().compareTo(s1.getSaleDate()));
                break;
        }
        displaySales(sales);
    }

    private void styleTable() {
        salesTable.setBackground(BACKGROUND_COLOR);
        salesTable.setForeground(TEXT_COLOR);
        salesTable.setGridColor(BORDER_COLOR);
        salesTable.setRowHeight(30);
        salesTable.getTableHeader().setBackground(BACKGROUND_COLOR);
        salesTable.getTableHeader().setForeground(TEXT_COLOR);
        salesTable.setSelectionBackground(ACCENT_COLOR);
        salesTable.setSelectionForeground(Color.WHITE);
        salesTable.setFont(TEXT_FIELD_FONT);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < salesTable.getColumnCount(); i++) {
            salesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        ((DefaultTableCellRenderer) salesTable.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });
        return button;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(TEXT_FIELD_FONT);
        textField.setBorder(new RoundedBorder(5));
        return textField;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JComboBox<String> createSortComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{
            SORT_BY_QUANTITY_ASC,
            SORT_BY_QUANTITY_DESC,
            SORT_BY_DATE_ASC,
            SORT_BY_DATE_DESC
        });
        styleComboBox(comboBox);
        comboBox.setSelectedItem(SORT_BY_DATE_ASC);
        return comboBox;
    }

    private void styleComboBox(JComboBox comboBox) {
        comboBox.setFont(TEXT_FIELD_FONT);
        comboBox.setBackground(BACKGROUND_COLOR);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBorder(new RoundedBorder(5));
    }

    private JLabel createMessageLabel() {
        JLabel label = new JLabel("");
        label.setFont(MESSAGE_FONT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void showMessage(String message, Color color) {
        messageLabel.setForeground(color);
        messageLabel.setText(message);
        Timer timer = new Timer(5000, e -> messageLabel.setText(""));
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
}

