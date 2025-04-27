package com.example.lab03;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab03.databinding.ActivityReviewBinding;

public class ReviewActivity extends AppCompatActivity {

    private ActivityReviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int correctas = getIntent().getIntExtra("correctas", 0);
        int incorrectas = getIntent().getIntExtra("incorrectas", 0);
        int noRespondidas = getIntent().getIntExtra("noRespondidas", 0);

        binding.textCorrectas.setText(String.valueOf(correctas));
        binding.textIncorrectas.setText(String.valueOf(incorrectas));
        binding.textNoRespondidas.setText(String.valueOf(noRespondidas));

        binding.btnVolverAJugar.setOnClickListener(v -> {
            Intent intent = new Intent(ReviewActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
