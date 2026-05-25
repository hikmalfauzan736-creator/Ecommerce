package com.tokoku.app.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Order {
    @SerializedName("id")
    public int id;

    @SerializedName("total_price")
    public double totalPrice;

    @SerializedName("status")
    public String status;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("items")
    public List<OrderItem> items;
}