package com.tokoku.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.tokoku.app.R;
import com.tokoku.app.adapter.ProductAdapter;
import com.tokoku.app.api.ApiClient;
import com.tokoku.app.api.ApiService;
import com.tokoku.app.databinding.ActivityMainBinding;
import com.tokoku.app.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ProductAdapter adapter;
    private List<Product> allProducts = new ArrayList<>();
    public static List<Product> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.toolbar);

        adapter = new ProductAdapter(this, new ArrayList<>(), product -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.id);
            startActivity(intent);
        });

        binding.rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvProducts.setAdapter(adapter);

        binding.fabCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class))
        );
        binding.fabScanner.setOnClickListener(v ->
                startActivity(new Intent(this, ScannerActivity.class))
        );

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
        });

        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update badge jumlah cart
        int totalCart = 0;
        for (Product p : cartItems) totalCart += p.quantity;
        binding.tvCartCount.setText(String.valueOf(totalCart));
        binding.tvCartCount.setVisibility(totalCart > 0 ? View.VISIBLE : View.GONE);
    }

    private void loadProducts() {
        binding.progressBar.setVisibility(View.VISIBLE);
        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        api.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    allProducts = response.body();
                    adapter.updateData(allProducts);
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Gagal memuat produk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterProducts(String query) {
        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.name.toLowerCase().contains(query.toLowerCase())) {
                filtered.add(p);
            }
        }
        adapter.updateData(filtered);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_orders) {
            startActivity(new Intent(this, OrdersActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            getSharedPreferences("tokoku_prefs", MODE_PRIVATE)
                    .edit().remove("token").apply();
            cartItems.clear();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}