package com.inventory.ui.reports;

import com.inventory.dao.ReportsDAO;
import com.inventory.model.Reports;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.text.NumberFormat;
import org.jfree.chart.plot.Plot;

public class ReportsPanel extends JPanel {

    private JTable reportsTable;
    private DefaultTableModel reportsTableModel;
    private ReportsDAO reportsDAO;
    private JPanel reportPanel;
    private JLabel messageLabel;
    private JButton refreshButton;
    private JPanel chartPanel;
    private JSplitPane splitPane;
    private JComboBox<String> sortComboBox;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTabbedPane tabbedPane;
    private JPanel supplierRevenuePanel;
    private JPanel productRevenuePanel;
    private JComboBox<String> supplierComboBox;
    private Map<String, Double> supplierTotalRevenueMap = new HashMap<>();
    private JPanel bottomPanel; // New panel for bottom controls
    private ButtonGroup productSortGroup;
    private JRadioButton productSortAscending;
    private JRadioButton productSortDescending;

    // Colors for the flat design
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color BORDER_COLOR = new Color(204, 204, 204);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);

    // Fonts
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TEXT_FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font MESSAGE_FONT = new Font("Segoe UI", Font.ITALIC, 12);

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Initialize report table
        reportsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportsTableModel.addColumn("Product Name");
        reportsTableModel.addColumn("Supplier Name");
        reportsTableModel.addColumn("Total Revenue");
        reportsTableModel.addColumn("Supplier Contribution (%)");
        reportsTable = new JTable(reportsTableModel);
        styleTable();

        // Add sorting
        sorter = new TableRowSorter<>(reportsTableModel);
        reportsTable.setRowSorter(sorter);

        // Initialize tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BACKGROUND_COLOR);

        // Initialize supplier revenue panel
        supplierRevenuePanel = new JPanel(new BorderLayout());
        supplierRevenuePanel.setBackground(BACKGROUND_COLOR);
        supplierRevenuePanel.setPreferredSize(new Dimension(400, 300));

        // Initialize product revenue panel
        productRevenuePanel = new JPanel(new BorderLayout()); // Change to BorderLayout
        productRevenuePanel.setBackground(BACKGROUND_COLOR);
        productRevenuePanel.setPreferredSize(new Dimension(400, 400)); // Increased height

        // Initialize supplier combo box
        supplierComboBox = new JComboBox<>();
        supplierComboBox.setFont(TEXT_FIELD_FONT);
        supplierComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductRevenueChart();
            }
        });

        // Initialize bottom panel
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout for buttons
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.add(new JLabel("Select Supplier:"));
        bottomPanel.add(supplierComboBox);

        productSortGroup = new ButtonGroup();
        productSortAscending = new JRadioButton("Ascending");
        productSortAscending.setBackground(BACKGROUND_COLOR);
        productSortAscending.setFont(TEXT_FIELD_FONT);
        productSortAscending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductRevenueChart();
            }
        });

        productSortDescending = new JRadioButton("Descending");
        productSortDescending.setBackground(BACKGROUND_COLOR);
        productSortDescending.setFont(TEXT_FIELD_FONT);
        productSortDescending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductRevenueChart();
            }
        });

        productSortGroup.add(productSortAscending);
        productSortGroup.add(productSortDescending);

        bottomPanel.add(new JLabel("  Sort:")); // Add some spacing
        bottomPanel.add(productSortAscending);
        bottomPanel.add(productSortDescending);

        // Add panels to tabbed pane
        tabbedPane.addTab("Revenue by Supplier", supplierRevenuePanel);
        tabbedPane.addTab("Revenue by Product", productRevenuePanel);

        // Initialize split pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(reportsTable), tabbedPane);
        splitPane.setResizeWeight(0.7);
        splitPane.setBorder(null);

        // Initialize reportPanel
        reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBackground(BACKGROUND_COLOR);

        // Message label
        messageLabel = createMessageLabel();

        // Initialize refresh button
        refreshButton = new JButton("Refresh");
        refreshButton.setFont(BUTTON_FONT);
        refreshButton.setBackground(ACCENT_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadReports();
            }
        });

        // Initialize sort combo box
        sortComboBox = new JComboBox<>(new String[]{"Default", "Percentage (Ascending)", "Percentage (Descending)"});
        sortComboBox.setFont(TEXT_FIELD_FONT);
        sortComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applySort();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(refreshButton);
        buttonPanel.add(new JLabel("Sort by: "));
        buttonPanel.add(sortComboBox);
        buttonPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        reportPanel.add(buttonPanel, BorderLayout.NORTH);
        reportPanel.add(splitPane, BorderLayout.CENTER);
        reportPanel.add(messageLabel, BorderLayout.SOUTH);

        add(reportPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH); // Add bottomPanel to the main ReportsPanel

        reportsDAO = new ReportsDAO();
        loadReports();
    }

    private void applySort() {
        String selectedOption = (String) sortComboBox.getSelectedItem();
        if (selectedOption != null) {
            switch (selectedOption) {
                case "Default":
                    sorter.setSortsOnUpdates(true);
                    sorter.setRowFilter(null);
                    sorter.setComparator(3, getDefaultPercentageComparator());
                    List<RowSorter.SortKey> sortKeys = null;
                    sorter.setSortKeys(sortKeys);
                    break;
                case "Percentage (Ascending)":
                    sorter.setSortsOnUpdates(true);
                    sorter.setRowFilter(null);
                    sorter.setComparator(3, getAscendingPercentageComparator());
                    sorter.setSortKeys(List.of(new RowSorter.SortKey(3, SortOrder.ASCENDING)));
                    break;
                case "Percentage (Descending)":
                    sorter.setSortsOnUpdates(true);
                    sorter.setRowFilter(null);
                    sorter.setComparator(3, getDescendingPercentageComparator());
                    sorter.setSortKeys(List.of(new RowSorter.SortKey(3, SortOrder.DESCENDING)));
                    break;
            }
        }
    }

    private Comparator<String> getDefaultPercentageComparator() {
        return new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                try {
                    double d1 = Double.parseDouble(s1.replace("%", ""));
                    double d2 = Double.parseDouble(s2.replace("%", ""));
                    return Double.compare(d1, d2);
                } catch (NumberFormatException e) {
                    return s1.compareTo(s2);
                }
            }
        };
    }

    private Comparator<String> getAscendingPercentageComparator() {
        return getDefaultPercentageComparator();
    }

    private Comparator<String> getDescendingPercentageComparator() {
        return new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return -getDefaultPercentageComparator().compare(s1, s2);
            }
        };
    }

    private void loadReports() {
        try {
            List<Reports> reports = reportsDAO.getProductSupplierSalesReport();
            reportsTableModel.setRowCount(0);
            if (reports.isEmpty()) {
                showMessage("No reports to display", TEXT_COLOR);
                supplierRevenuePanel.removeAll();
                supplierRevenuePanel.repaint();
                productRevenuePanel.removeAll();
                productRevenuePanel.repaint();
                supplierComboBox.removeAllItems();
            } else {
                showMessage("Reports Loaded", SUCCESS_COLOR);
                supplierTotalRevenueMap.clear();
                supplierComboBox.removeAllItems();
                for (Reports report : reports) {
                    String formattedContribution = String.format("%.2f%%", report.getSupplierContribution() * 100);
                    reportsTableModel.addRow(new Object[]{
                            report.getProductName(),
                            report.getSupplierName(),
                            report.getTotalRevenue(),
                            formattedContribution
                    });
                    // Calculate total revenue per supplier
                    double supplierRevenue = supplierTotalRevenueMap.getOrDefault(report.getSupplierName(), 0.0);
                    supplierRevenue += report.getTotalRevenue();
                    supplierTotalRevenueMap.put(report.getSupplierName(), supplierRevenue);
                    if (supplierComboBox.getItemCount() == 0 || !supplierComboBox.getSelectedItem().equals(report.getSupplierName())){
                         supplierComboBox.addItem(report.getSupplierName());
                    }
                }
                createSupplierRevenueChart();
                updateProductRevenueChart();
                applySort();
            }

        } catch (SQLException e) {
            showMessage("Error loading reports: " + e.getMessage(), ERROR_COLOR);
            e.printStackTrace();
        }
    }

    private void createSupplierRevenueChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Double> entry : supplierTotalRevenueMap.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Total Revenue by Supplier",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new GradientPaint(0, 0, new Color(240, 240, 240), 0, 400, BACKGROUND_COLOR)); // Subtle gradient
        plot.setLabelFont(TEXT_FIELD_FONT);
        plot.setLabelBackgroundPaint(new Color(230, 230, 230, 200)); // Semi-transparent label background

        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0} = {1} ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance());
        plot.setLabelGenerator(labelGenerator);

        // Modern color palette
        plot.setSectionPaint("Supplier A", new Color(66, 134, 244));   // Strong blue
        plot.setSectionPaint("Supplier B", new Color(114, 183, 79));   // Vibrant green
        plot.setSectionPaint("Supplier C", new Color(250, 170, 70));   // Warm orange
        plot.setShadowPaint(new Color(150, 150, 150, 80));        // Subtle shadow
        plot.setOutlinePaint(null);                              // No outline

        ChartPanel panel = new ChartPanel(chart);
        panel.setBackground(BACKGROUND_COLOR); // Panel background
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Increased padding

        supplierRevenuePanel.removeAll();
        supplierRevenuePanel.add(panel, BorderLayout.CENTER);
        supplierRevenuePanel.revalidate();
        supplierRevenuePanel.repaint();
    }

    private void updateProductRevenueChart() {
        String selectedSupplier = (String) supplierComboBox.getSelectedItem();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Reports> productRevenueList = null;

        try {
            List<Reports> reports = reportsDAO.getProductSupplierSalesReport();
            productRevenueList = reports;
            for (Reports report : reports) {
                if (report.getSupplierName().equals(selectedSupplier)) {
                    dataset.addValue(report.getTotalRevenue(), report.getProductName(), report.getProductName());
                }
            }
        } catch (SQLException e) {
            showMessage("Error loading reports: " + e.getMessage(), ERROR_COLOR);
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Revenue by Product for " + selectedSupplier,
                "Product",
                "Revenue",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new GradientPaint(0, 0, new Color(240, 240, 240), 0, 400, BACKGROUND_COLOR)); // Subtle gradient
        plot.getDomainAxis().setTickLabelFont(TEXT_FIELD_FONT);
        plot.getRangeAxis().setTickLabelFont(TEXT_FIELD_FONT);
        plot.setOutlinePaint(null);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new GradientPaint(0, 0, new Color(52, 152, 219), 0, 400, new Color(80, 180, 247))); // Gradient bars
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter()); // Standard painter for gradients

        // Reduced gridline opacity
        plot.setRangeGridlinePaint(new Color(204, 204, 204, 150));
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(false); // No vertical gridlines

        plot.getDomainAxis().setCategoryLabelPositions(org.jfree.chart.axis.CategoryLabelPositions.UP_45);

        chart.getTitle().setFont(LABEL_FONT);

        ChartPanel panel = new ChartPanel(chart);
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Increased padding
        productRevenuePanel.removeAll();
        productRevenuePanel.add(panel, BorderLayout.CENTER);
        productRevenuePanel.revalidate();
        productRevenuePanel.repaint();

        if (productSortAscending.isSelected()) {
            sortProductRevenueChart(productRevenueList, true);
        } else if (productSortDescending.isSelected()) {
            sortProductRevenueChart(productRevenueList, false);
        }
    }

    private void sortProductRevenueChart(List<Reports> reports, boolean ascending) {
        String selectedSupplier = (String) supplierComboBox.getSelectedItem();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (reports != null) {
            reports.sort((r1, r2) -> {
                if (r1.getSupplierName().equals(selectedSupplier) && r2.getSupplierName().equals(selectedSupplier)) {
                    return Double.compare(r1.getTotalRevenue(), r2.getTotalRevenue());
                }
                return 0;
            });

            if (!ascending) {
                java.util.Collections.reverse(reports);
            }

            for (Reports report : reports) {
                if (report.getSupplierName().equals(selectedSupplier)) {
                    dataset.addValue(report.getTotalRevenue(), report.getProductName(), report.getProductName());
                }
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Revenue by Product for " + selectedSupplier,
                    "Product",
                    "Revenue",
                    dataset
            );

            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(new GradientPaint(0, 0, new Color(240, 240, 240), 0, 400, BACKGROUND_COLOR)); // Subtle gradient
            plot.getDomainAxis().setTickLabelFont(TEXT_FIELD_FONT);
            plot.getRangeAxis().setTickLabelFont(TEXT_FIELD_FONT);
            plot.setOutlinePaint(null);

            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new GradientPaint(0, 0, new Color(52, 152, 219), 0, 400, new Color(80, 180, 247))); // Gradient bars
            renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter()); // Standard painter for gradients

            // Reduced gridline opacity
            plot.setRangeGridlinePaint(new Color(204, 204, 204, 150));
            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinesVisible(false); // No vertical gridlines

            plot.getDomainAxis().setCategoryLabelPositions(org.jfree.chart.axis.CategoryLabelPositions.UP_45);

            chart.getTitle().setFont(LABEL_FONT);

            ChartPanel panel = new ChartPanel(chart);
            panel.setBackground(BACKGROUND_COLOR);
            panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Increased padding
            productRevenuePanel.removeAll();
            productRevenuePanel.add(panel, BorderLayout.CENTER);
            productRevenuePanel.revalidate();
            productRevenuePanel.repaint();
        }
    }

    private void styleTable() {
        reportsTable.setBackground(BACKGROUND_COLOR);
        reportsTable.setForeground(TEXT_COLOR);
        reportsTable.setGridColor(BORDER_COLOR);
        reportsTable.setRowHeight(30);
        reportsTable.getTableHeader().setBackground(BACKGROUND_COLOR);
        reportsTable.getTableHeader().setForeground(TEXT_COLOR);
        reportsTable.setSelectionBackground(ACCENT_COLOR);
        reportsTable.setSelectionForeground(Color.WHITE);
        reportsTable.setFont(TEXT_FIELD_FONT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < reportsTable.getColumnCount(); i++) {
            reportsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        ((DefaultTableCellRenderer) reportsTable.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
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
}