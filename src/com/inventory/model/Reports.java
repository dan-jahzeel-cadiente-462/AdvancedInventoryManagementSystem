package com.inventory.model;

public class Reports {
    private String productName;
    private String supplierName;
    private double totalRevenue;
    private double supplierContribution;

    public Reports() {
    }

    public Reports(String productName, String supplierName, double totalRevenue, double supplierContribution) {
        this.productName = productName;
        this.supplierName = supplierName;
        this.totalRevenue = totalRevenue;
        this.supplierContribution = supplierContribution;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getSupplierContribution() {
        return supplierContribution;
    }

    public void setSupplierContribution(double supplierContribution) {
        this.supplierContribution = supplierContribution;
    }
}