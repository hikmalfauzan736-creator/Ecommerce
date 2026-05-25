package com.tokoku.app.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.tokoku.app.api.ApiClient;
import com.tokoku.app.api.ApiService;
import com.tokoku.app.databinding.ActivityProductDetailBinding;
import com.tokoku.app.model.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private Product currentProduct;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Aman kalau ActionBar null
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detail Produk");
        }

        int productId = getIntent().getIntExtra("product_id", -1);

        // Tombol minus
        binding.btnMinus.setOnClickListener(v -> {

            if (quantity > 1) {
                quantity--;
                binding.tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // Tombol plus
        binding.btnPlus.setOnClickListener(v -> {

            if (currentProduct == null) {

                Toast.makeText(
                        this,
                        "Produk belum dimuat",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            int stock = currentProduct.stock != null
                    ? currentProduct.stock
                    : 0;

            if (stock <= 0) {

                Toast.makeText(
                        this,
                        "Stok habis",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (quantity < stock) {

                quantity++;

                binding.tvQuantity.setText(
                        String.valueOf(quantity)
                );
            }
        });

        // Tombol tambah keranjang
        binding.btnAddToCart.setOnClickListener(v -> addToCart());

        loadProduct(productId);
    }

    private void loadProduct(int id) {

        binding.progressBar.setVisibility(android.view.View.VISIBLE);

        ApiService api = ApiClient
                .getClient(this)
                .create(ApiService.class);

        api.getProductById(id).enqueue(new Callback<Product>() {

            @Override
            public void onResponse(Call<Product> call,
                                   Response<Product> response) {

                binding.progressBar
                        .setVisibility(android.view.View.GONE);

                if (response.isSuccessful()
                        && response.body() != null) {

                    currentProduct = response.body();

                    displayProduct(currentProduct);
                }
            }

            @Override
            public void onFailure(Call<Product> call,
                                  Throwable t) {

                binding.progressBar
                        .setVisibility(android.view.View.GONE);

                Toast.makeText(
                        ProductDetailActivity.this,
                        "Gagal memuat produk",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void displayProduct(Product p) {

        // Aman kalau ActionBar null
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(p.name);
        }

        binding.tvName.setText(p.name);

        binding.tvCategory.setText(
                p.category != null ? p.category : "-"
        );

        binding.tvPrice.setText(
                "Rp " + String.format("%,.0f", p.price)
        );

        binding.tvDescription.setText(
                p.description != null
                        ? p.description
                        : "Tidak ada deskripsi."
        );

        binding.tvStock.setText(
                (p.stock != null && p.stock > 0)
                        ? "Stok tersedia: " + p.stock
                        : "Stok habis"
        );

        binding.btnAddToCart.setEnabled(p.stock != null && p.stock > 0);

        // Load gambar aman
        String imageUrl = p.imageUrl;

        if (imageUrl == null || imageUrl.isEmpty()) {

            binding.ivProduct.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );

        } else {

            imageUrl = imageUrl.replace(
                    "127.0.0.1",
                    "192.168.100.9"
            );

            imageUrl = imageUrl.replace(
                    "localhost",
                    "192.168.100.9"
            );

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(binding.ivProduct);
        }
    }

    private void addToCart() {

        if (currentProduct == null) return;

        for (Product item : MainActivity.cartItems) {

            if (item.id == currentProduct.id) {

                item.quantity += quantity;

                Toast.makeText(
                        this,
                        "Jumlah diperbarui di keranjang!",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

                return;
            }
        }

        Product cartProduct = new Product();

        cartProduct.id = currentProduct.id;
        cartProduct.name = currentProduct.name;
        cartProduct.price = currentProduct.price;
        cartProduct.stock = currentProduct.stock;
        cartProduct.imageUrl = currentProduct.imageUrl;
        cartProduct.quantity = quantity;

        MainActivity.cartItems.add(cartProduct);

        Toast.makeText(
                this,
                currentProduct.name + " ditambahkan!",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}