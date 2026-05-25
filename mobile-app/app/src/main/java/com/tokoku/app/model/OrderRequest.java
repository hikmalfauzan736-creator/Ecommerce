package com.tokoku.app.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderRequest {
    @SerializedName("items")
    public List<OrderItemRequest> items;

    public OrderRequest(List<OrderItemRequest> items) {
        this.items = items;
    }

    public static class OrderItemRequest {
        @SerializedName("product_id")
        public int productId;

        @SerializedName("quantity")
        public int quantity;

        public OrderItemRequest(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}