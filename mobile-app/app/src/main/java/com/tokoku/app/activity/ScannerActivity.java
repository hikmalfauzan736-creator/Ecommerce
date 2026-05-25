package com.tokoku.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tokoku.app.databinding.ActivityScannerBinding;

public class ScannerActivity extends AppCompatActivity {

    private ActivityScannerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cegah crash kalau ActionBar null
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Scan Barcode Produk");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Tombol scan
        binding.btnStartScan.setOnClickListener(v -> startScanner());

        // Langsung buka scanner
        startScanner();
    }

    private void startScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Arahkan kamera ke barcode produk");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);

        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (result != null) {

            if (result.getContents() != null) {

                try {

                    int productId = Integer.parseInt(result.getContents());

                    Intent intent = new Intent(this, ProductDetailActivity.class);
                    intent.putExtra("product_id", productId);

                    startActivity(intent);
                    finish();

                } catch (NumberFormatException e) {

                    Toast.makeText(
                            this,
                            "Barcode tidak valid: " + result.getContents(),
                            Toast.LENGTH_SHORT
                    ).show();

                    binding.tvScanResult.setText(
                            "Hasil scan: " + result.getContents()
                                    + "\n(bukan ID produk)"
                    );
                }

            } else {
                finish();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}