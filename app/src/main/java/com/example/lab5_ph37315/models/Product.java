package com.example.lab5_ph37315.models;

public class Product {
    private String _id;
    private String name;

    public Product(String name, double gia) {
        this.name = name;
        this.gia = gia;
    }

    private double gia;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public Product() {
    }

    public Product(String id, String name, double gia) {
        this._id = id;
        this.name = name;
        this.gia = gia;
    }
}
