package com.tokoku.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tokoku.app.adapter.OrderAdapter;
import com.tokoku.app.api.ApiClient;
import com.tokoku.app.api.ApiService;
import com.tokoku.app.databinding.ActivityOrdersBinding;
import com.tokoku.app.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {

    private ActivityOrdersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrdersBinding.inflate(
                getLayoutInflater()
        );

        setContentView(binding.getRoot());

        // Aman kalau ActionBar null
        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(
                    "Pesanan Saya"
            );

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        binding.rvOrders.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadOrders();
    }

    private void loadOrders() {

        binding.progressBar.setVisibility(
                View.VISIBLE
        );

        ApiService api = ApiClient
                .getClient(this)
                .create(ApiService.class);

        api.getMyOrders().enqueue(
                new Callback<List<Order>>() {

                    @Override
                    public void onResponse(
                            Call<List<Order>> call,
                            Response<List<Order>> response
                    ) {

                        binding.progressBar.setVisibility(
                                View.GONE
                        );

                        if (response.isSuccessful()
                                && response.body() != null) {

                            List<Order> orders =
                                    response.body();

                            if (orders.isEmpty()) {

                                binding.tvEmpty
                                        .setVisibility(View.VISIBLE);

                            } else {

                                binding.tvEmpty
                                        .setVisibility(View.GONE);

                                binding.rvOrders.setAdapter(
                                        new OrderAdapter(orders)
                                );
                            }
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Order>> call,
                            Throwable t
                    ) {

                        binding.progressBar.setVisibility(
                                View.GONE
                        );

                        Toast.makeText(
                                OrdersActivity.this,
                                "Gagal memuat pesanan",
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