package com.example.myapplication;

import android.graphics.Bitmap;

public class Item {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuanlity(int q) {
        quanlity = q;
    }
    public int getQuanlity() {
        return quanlity;
    }

    String id;
    String name;
    Bitmap image;
    int price;
    int quanlity;

    public Item() {

    }

    public Item(String id, String name, Bitmap image, int price, int quanlity) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quanlity = quanlity;
    }




}
