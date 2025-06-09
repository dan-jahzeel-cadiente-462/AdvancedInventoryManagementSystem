package com.inventory.model;

public class Product {
    private int productID;
    private String name;
    private String category;
    private double price;
    private int userID;

    public Product() {
    }

    public Product(int productID, String name, String category, double price, int userID) { // Removed user
        this.productID = productID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.userID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return name;
    }
}