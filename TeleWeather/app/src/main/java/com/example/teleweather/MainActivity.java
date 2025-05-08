package com.example.teleweather;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Listener para comprobar conexión
        binding.btnComprobarConexion.setOnClickListener(view -> comprobarConexion());

        // Listener botón Comenzar
        binding.btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TeletriviaActivity.class);

                // Se manda dificultad en inglesss
                String dificultadSeleccionada = binding.spinnerDificultad.getText().toString().trim();
                String dificultadAPI = "";

                switch (dificultadSeleccionada.toLowerCase()) {
                    case "fácil":
                        dificultadAPI = "easy";
                        break;
                    case "medio":
                        dificultadAPI = "medium";
                        break;
                    case "difícil":
                        dificultadAPI = "hard";
                        break;
                }

                intent.putExtra("cantidad", Integer.parseInt(binding.editTextCantidad.getText().toString().trim()));
                intent.putExtra("dificultad", dificultadAPI);

                startActivity(intent);
            }
        });
    }

    private void comprobarConexion() {

        // se verifica la conexión a Internet
        if (hayConexionInternet()) {
            Toast.makeText(this, "Success Toast", Toast.LENGTH_SHORT).show();
            binding.btnComenzar.setEnabled(true);
        } else {
            Toast.makeText(this, "Error Toast", Toast.LENGTH_SHORT).show();
            binding.btnComenzar.setEnabled(false);
        }
    }

    private boolean hayConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }
        return false;
    }
}