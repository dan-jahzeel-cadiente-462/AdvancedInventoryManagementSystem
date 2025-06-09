package com.inventory.model;

import java.util.Date;

public class Stock {
    private int stockID;
    private int productID;
    private int supplierID;
    private int quantityAdded;
    private Date dateAdded;

    public Stock() {
    }

    public Stock(int stockID, int productID, int supplierID, int quantityAdded, Date dateAdded) {
        this.stockID = stockID;
        this.productID = productID;
        this.supplierID = supplierID;
        this.quantityAdded = quantityAdded;
        this.dateAdded = dateAdded;
    }

    public int getStockID() {
        return stockID;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getQuantityAdded() {
        return quantityAdded;
    }

    public void setQuantityAdded(int quantityAdded) {
        this.quantityAdded = quantityAdded;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stockID=" + stockID +
                ", productID=" + productID +
                ", supplierID=" + supplierID +
                ", quantityAdded=" + quantityAdded +
                ", dateAdded=" + dateAdded +
                '}';
    }
}