package com.inventory.ui.supplier;

import com.inventory.dao.SuppliersDAO;
import com.inventory.dao.SuppliersDAOImpl;
import com.inventory.model.Supplier;
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
import java.util.List;

public class SupplierManagementPanel extends JPanel {

    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField contactInfoField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private boolean isViewOnly;
    private JPanel inputPanel;
    private JLabel messageLabel;

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

    public SupplierManagementPanel(boolean viewOnly) {
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
        tableModel.addColumn("Contact Info");
        supplierTable = new JTable(tableModel);
        supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only allow single row selection
        styleTable();

        JScrollPane scrollPane = new JScrollPane(supplierTable);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(scrollPane, BorderLayout.CENTER);

        if (!isViewOnly) {
            // Input panel for adding/updating suppliers
            inputPanel = new JPanel(new GridBagLayout());
            inputPanel.setBackground(BACKGROUND_COLOR);
            inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;

            JLabel nameLabel = createLabel("Name:");
            nameField = createTextField();
            JLabel contactInfoLabel = createLabel("Contact Info:");
            contactInfoField = createTextField();
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
            inputPanel.add(contactInfoLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            inputPanel.add(contactInfoField, gbc);

            // Buttons
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(BACKGROUND_COLOR);
            buttonPanel.add(addButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);
            inputPanel.add(buttonPanel, gbc);

            // Message Label
            messageLabel = createMessageLabel();
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.CENTER;
            inputPanel.add(messageLabel, gbc);


            add(inputPanel, BorderLayout.NORTH);

            // Action listeners for buttons
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addSupplier();
                }
            });

            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateSupplier();
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteSupplier();
                }
            });

            // Add a MouseListener to the table
            supplierTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int selectedRow = supplierTable.getSelectedRow();
                    if (selectedRow != -1) {
                        nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                        contactInfoField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    }
                }
            });
        }
        loadSuppliers();
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
        supplierTable.setBackground(BACKGROUND_COLOR);
        supplierTable.setForeground(TEXT_COLOR);
        supplierTable.setGridColor(BORDER_COLOR);
        supplierTable.setRowHeight(30);
        supplierTable.getTableHeader().setBackground(BACKGROUND_COLOR);
        supplierTable.getTableHeader().setForeground(TEXT_COLOR);
        supplierTable.setSelectionBackground(ACCENT_COLOR);
        supplierTable.setSelectionForeground(Color.WHITE);
        supplierTable.setFont(TEXT_FIELD_FONT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < supplierTable.getColumnCount(); i++) {
            supplierTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void loadSuppliers() {
        try {
            SuppliersDAOImpl supplierDAO = new SuppliersDAOImpl();
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            tableModel.setRowCount(0);
            for (Supplier supplier : suppliers) {
                tableModel.addRow(new Object[]{supplier.getSupplierID(), supplier.getName(), supplier.getContactInfo()});
            }
        } catch (SQLException e) {
            showMessage("Error loading suppliers: " + e.getMessage(), false);
        }
    }

    private void addSupplier() {
        String name = nameField.getText();
        String contactInfo = contactInfoField.getText();

        if (name.isEmpty() || contactInfo.isEmpty()) {
            showMessage("Please fill in all fields.", false);
            return;
        }

        try {
            Supplier supplier = new Supplier(0, name, contactInfo);
            SuppliersDAOImpl supplierDAO = new SuppliersDAOImpl();
            supplierDAO.addSupplier(supplier);
            loadSuppliers();
            clearInputFields();
            showMessage("Supplier added successfully.", true);
        } catch (SQLException e) {
            showMessage("Error adding supplier: " + e.getMessage(), false);
        }
    }

    private void updateSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Please select a supplier to update.", false);
            return;
        }

        int supplierID = (int) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String contactInfo = contactInfoField.getText();

        if (name.isEmpty() || contactInfo.isEmpty()) {
            showMessage("Please fill in all fields.", false);
            return;
        }

        try {
            Supplier supplier = new Supplier(supplierID, name, contactInfo);
            SuppliersDAOImpl supplierDAO = new SuppliersDAOImpl();
            supplierDAO.updateSupplier(supplier);
            loadSuppliers();
            clearInputFields();
            showMessage("Supplier updated successfully.", true);
        } catch (SQLException e) {
            showMessage("Error updating supplier: " + e.getMessage(), false);
        }
    }

    private void deleteSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Please select a supplier to delete.", false);
            return;
        }

        int supplierID = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            SuppliersDAOImpl supplierDAO = new SuppliersDAOImpl();
            supplierDAO.deleteSupplier(supplierID);
            loadSuppliers();
            clearInputFields();
            showMessage("Supplier deleted successfully.", true);
        } catch (SQLException e) {
            showMessage("Error deleting supplier: " + e.getMessage(), false);
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        contactInfoField.setText("");
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message);
        messageLabel.setForeground(success ? SUCCESS_COLOR : ERROR_COLOR);

        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageLabel.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

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

