package com.example.sdl.OrderSummary;

public class Order {
    int sNo;
    String item;
    int quantity;
    int price;
    int totalPrice;

    public Order() { }


    public Order(int sNo, String item,int quantity, int price) {
        this.sNo = sNo;
        this.item = item;
        this.quantity = quantity;
        this.price =price;
        this.totalPrice= this.price;
    }


    public int getsNo() {
        return sNo;
    }

    public void setsNo(int sNo) {
        this.sNo = sNo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setTotalPrice() {
        this.totalPrice = this.price*this.quantity;
    }
}
