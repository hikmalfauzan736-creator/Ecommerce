package com.tokoku.app.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("price")
    public double price;

    // dibuat Integer supaya aman kalau null
    @SerializedName("stock")
    public Integer stock;

    @SerializedName("category")
    public String category;

    @SerializedName("image_url")
    public String imageUrl;

    // untuk keranjang (tidak dari API)
    public int quantity = 1;
}