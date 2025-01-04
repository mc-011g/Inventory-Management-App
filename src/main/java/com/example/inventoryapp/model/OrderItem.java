package com.example.inventoryapp.model;

public class OrderItem {

    private Product product;
    private int quantity;
    private boolean selected;

    public OrderItem(Product product, int quantity, boolean selected) {
        this.product = product;
        this.quantity = quantity;
        this.selected = selected;
    }

    public OrderItem() {
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
