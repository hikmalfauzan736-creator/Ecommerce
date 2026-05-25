package com.tokoku.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tokoku.app.R;
import com.tokoku.app.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> products, OnProductClickListener listener) {
        this.context  = context;
        this.products = products;
        this.listener = listener;
    }

    public void updateData(List<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvName.setText(product.name);
        holder.tvCategory.setText(product.category != null ? product.category : "");
        holder.tvPrice.setText("Rp " + String.format("%,.0f", product.price));
        holder.tvStock.setText(product.stock > 0 ? "Stok: " + product.stock : "Habis");
        holder.tvStock.setTextColor(context.getColor(
                product.stock > 0 ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));

        Glide.with(context)
                .load(product.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(holder.ivProduct);

        holder.itemView.setOnClickListener(v -> listener.onClick(product));
    }

    @Override
    public int getItemCount() { return products.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvCategory, tvPrice, tvStock;

        ViewHolder(View itemView) {
            super(itemView);
            ivProduct  = itemView.findViewById(R.id.ivProduct);
            tvName     = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice    = itemView.findViewById(R.id.tvPrice);
            tvStock    = itemView.findViewById(R.id.tvStock);
        }
    }
}