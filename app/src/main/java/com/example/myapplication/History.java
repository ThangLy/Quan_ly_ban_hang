package com.example.myapplication;

public class History {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int cost) {
        this.total = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String code;
    Item item;
    int amount;
    int total;
    String time;

    public History(){

    }
    public History(String c, Item i, int a, int m, String t) {
        code = c;
        item = i;
        amount = a;
        total = m;
        time = t;
    }



}
