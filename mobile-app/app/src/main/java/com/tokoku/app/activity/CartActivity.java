package com.tokoku.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tokoku.app.adapter.CartAdapter;
import com.tokoku.app.api.ApiClient;
import com.tokoku.app.api.ApiService;
import com.tokoku.app.databinding.ActivityCartBinding;
import com.tokoku.app.model.Order;
import com.tokoku.app.model.OrderRequest;
import com.tokoku.app.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Aman kalau ActionBar null
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Keranjang Belanja");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new CartAdapter(
                MainActivity.cartItems,
                this::updateTotal
        );

        binding.rvCart.setLayoutManager(
                new LinearLayoutManager(this)
        );

        binding.rvCart.setAdapter(adapter);

        binding.btnCheckout.setOnClickListener(
                v -> doCheckout()
        );

        updateTotal();
    }

    public void updateTotal() {

        double total = 0;

        for (Product item : MainActivity.cartItems) {
            total += item.price * item.quantity;
        }

        binding.tvTotal.setText(
                "Total: Rp " + String.format("%,.0f", total)
        );

        if (MainActivity.cartItems.isEmpty()) {

            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.rvCart.setVisibility(View.GONE);

            binding.btnCheckout.setEnabled(false);

        } else {

            binding.tvEmpty.setVisibility(View.GONE);
            binding.rvCart.setVisibility(View.VISIBLE);

            binding.btnCheckout.setEnabled(true);
        }
    }

    private void doCheckout() {

        List<OrderRequest.OrderItemRequest> items =
                new ArrayList<>();

        for (Product p : MainActivity.cartItems) {

            items.add(
                    new OrderRequest.OrderItemRequest(
                            p.id,
                            p.quantity
                    )
            );
        }

        binding.btnCheckout.setEnabled(false);

        binding.progressBar.setVisibility(View.VISIBLE);

        ApiService api = ApiClient
                .getClient(this)
                .create(ApiService.class);

        api.createOrder(
                new OrderRequest(items)
        ).enqueue(new Callback<Order>() {

            @Override
            public void onResponse(Call<Order> call,
                                   Response<Order> response) {

                binding.progressBar
                        .setVisibility(View.GONE);

                if (response.isSuccessful()
                        && response.body() != null) {

                    MainActivity.cartItems.clear();

                    adapter.notifyDataSetChanged();

                    updateTotal();

                    Order order = response.body();

                    Toast.makeText(
                            CartActivity.this,
                            "Pesanan #" + order.id
                                    + " berhasil!\nTotal: Rp "
                                    + String.format("%,.0f",
                                    order.totalPrice),
                            Toast.LENGTH_LONG
                    ).show();

                } else {

                    binding.btnCheckout.setEnabled(true);

                    Toast.makeText(
                            CartActivity.this,
                            "Checkout gagal",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call,
                                  Throwable t) {

                binding.progressBar
                        .setVisibility(View.GONE);

                binding.btnCheckout.setEnabled(true);

                Toast.makeText(
                        CartActivity.this,
                        "Koneksi gagal",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}