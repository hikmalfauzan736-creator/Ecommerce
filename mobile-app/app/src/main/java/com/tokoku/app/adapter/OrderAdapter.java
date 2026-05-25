package com.tokoku.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokoku.app.R;
import com.tokoku.app.model.Order;
import com.tokoku.app.model.OrderItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderId.setText("Pesanan #" + order.id);
        holder.tvTotal.setText("Total: Rp " + String.format("%,.0f", order.totalPrice));
        holder.tvDate.setText(order.createdAt != null ? order.createdAt.substring(0, 10) : "-");

        // Status badge
        switch (order.status) {
            case "paid":
                holder.tvStatus.setText("✅ Dibayar");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"));
                holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
                break;
            case "shipped":
                holder.tvStatus.setText("🚚 Dikirim");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#E3F2FD"));
                holder.tvStatus.setTextColor(Color.parseColor("#1565C0"));
                break;
            case "done":
                holder.tvStatus.setText("🎉 Selesai");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#F3E5F5"));
                holder.tvStatus.setTextColor(Color.parseColor("#6A1B9A"));
                break;
            default:
                holder.tvStatus.setText("⏳ Menunggu");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FFF3E0"));
                holder.tvStatus.setTextColor(Color.parseColor("#E65100"));
        }

        // Items list
        StringBuilder sb = new StringBuilder();
        if (order.items != null) {
            for (OrderItem item : order.items) {
                sb.append("• Produk ID ").append(item.productId)
                        .append(" x").append(item.quantity)
                        .append(" = Rp ").append(String.format("%,.0f", item.price * item.quantity))
                        .append("\n");
            }
        }
        holder.tvItems.setText(sb.toString().trim());
    }

    @Override
    public int getItemCount() { return orders.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvStatus, tvTotal, tvDate, tvItems;

        ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvStatus  = itemView.findViewById(R.id.tvStatus);
            tvTotal   = itemView.findViewById(R.id.tvTotal);
            tvDate    = itemView.findViewById(R.id.tvDate);
            tvItems   = itemView.findViewById(R.id.tvItems);
        }
    }
}