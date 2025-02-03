package com.example.inventoryapp.dto;

public class OrderItemCategorySales {

    private String category;
    private double salesValue;

    public String getCategory() {
        return category;
    }

    public double getSalesValue() {
        return salesValue;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSalesValue(double salesValue) {
        this.salesValue = salesValue;
    }

    public OrderItemCategorySales(String category, double salesValue) {
        this.category = category;
        this.salesValue = salesValue;
    }

    public OrderItemCategorySales() {
    }

}
