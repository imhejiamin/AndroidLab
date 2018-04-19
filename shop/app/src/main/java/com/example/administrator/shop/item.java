package com.example.administrator.shop;



public class item {

    public String name; //商品名称
    public String price;  //商品价格
    public String belong;  //商品属性

    public item(String n, String p, String b) {
        this.name = n;
        this.price = p;
        this.belong = b;
    }

    public String getName() {return name;}

    public String getPrice() {
        return price;
    }

    public String getBelong() {
        return belong;
    }
}

