package com.tokoku.app.model;
import com.google.gson.annotations.SerializedName;

public class OrderItem {
    @SerializedName("product_id")
    public int productId;

    @SerializedName("quantity")
    public int quantity;

    @SerializedName("price")
    public double price;
}