package com.example.lab03;

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

import com.example.lab03.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private String[] categorias = {"Cultura General", "Libros", "Películas", "Música", "Computación", "Matemática", "Deportes", "Historia"};
    private String[] dificultades = {"fácil", "medio", "difícil"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Llenar Categoría
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categorias);
        ((AutoCompleteTextView) binding.spinnerCategoria).setAdapter(adapterCategorias);

        // Llenar Dificultad
        ArrayAdapter<String> adapterDificultades = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dificultades);
        ((AutoCompleteTextView) binding.spinnerDificultad).setAdapter(adapterDificultades);

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

        String categoriaSeleccionada = binding.spinnerCategoria.getText().toString().trim();
        String cantidadStr = binding.editTextCantidad.getText().toString().trim();
        String dificultadSeleccionada = binding.spinnerDificultad.getText().toString().trim();

        if (categoriaSeleccionada.isEmpty()) {
            Toast.makeText(this, "Debe seleccionar una categoría", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Debe ingresar una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);
        if (cantidad <= 0) {
            Toast.makeText(this, "La cantidad debe ser positiva", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dificultadSeleccionada.isEmpty()) {
            Toast.makeText(this, "Debe seleccionar una dificultad", Toast.LENGTH_SHORT).show();
            return;
        }

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