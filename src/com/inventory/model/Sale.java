package com.inventory.model;

import java.util.Date;

public class Sale {
    private int saleID;
    private int productID;
    private int quantitySold;
    private Date saleDate;
    private double totalAmount;

    public Sale() {
    }

    public Sale(int saleID, int productID, int quantitySold, Date saleDate, double totalAmount) {
        this.saleID = saleID;
        this.productID = productID;
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleID=" + saleID +
                ", productID=" + productID +
                ", quantitySold=" + quantitySold +
                ", saleDate=" + saleDate +
                ", totalAmount=" + totalAmount +
                '}';
    }
}