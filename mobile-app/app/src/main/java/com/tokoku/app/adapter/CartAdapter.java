package com.tokoku.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokoku.app.R;
import com.tokoku.app.model.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Product> cartItems;
    private Runnable onCartChanged;

    public CartAdapter(List<Product> cartItems, Runnable onCartChanged) {
        this.cartItems      = cartItems;
        this.onCartChanged  = onCartChanged;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = cartItems.get(position);
        holder.tvName.setText(item.name);
        holder.tvPrice.setText("Rp " + String.format("%,.0f", item.price));
        holder.tvQty.setText("x" + item.quantity);
        holder.tvSubtotal.setText("Rp " + String.format("%,.0f", item.price * item.quantity));

        holder.btnMinus.setOnClickListener(v -> {
            if (item.quantity > 1) {
                item.quantity--;
                notifyItemChanged(position);
                onCartChanged.run();
            }
        });

        holder.btnPlus.setOnClickListener(v -> {
            if (item.quantity < item.stock) {
                item.quantity++;
                notifyItemChanged(position);
                onCartChanged.run();
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            onCartChanged.run();
        });
    }

    @Override
    public int getItemCount() { return cartItems.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQty, tvSubtotal;
        ImageButton btnMinus, btnPlus, btnRemove;

        ViewHolder(View itemView) {
            super(itemView);
            tvName    = itemView.findViewById(R.id.tvName);
            tvPrice   = itemView.findViewById(R.id.tvPrice);
            tvQty     = itemView.findViewById(R.id.tvQty);
            tvSubtotal= itemView.findViewById(R.id.tvSubtotal);
            btnMinus  = itemView.findViewById(R.id.btnMinus);
            btnPlus   = itemView.findViewById(R.id.btnPlus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}