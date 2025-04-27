package com.example.lab03;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.lab03.databinding.ActivityTeletriviaBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.lab03.bean.Preguntas;
import com.example.lab03.bean.Respuestas;

public class TeletriviaActivity extends AppCompatActivity {

    private ActivityTeletriviaBinding binding;
    private int tiempoTotal;
    private Thread contadorThread;
    private boolean sigueCuenta = true;

    private int posicionPregunta = 0;

    private ApiService apiService;
    private List<Preguntas> listaPreguntas = new ArrayList<>();
    private int correctas = 0;
    private int incorrectas = 0;
    private int enBlanco = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeletriviaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int cantidad = getIntent().getIntExtra("cantidad", 1);
        String dificultad = getIntent().getStringExtra("dificultad");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        obtenerPreguntas(cantidad, dificultad);

        int segundosPorPregunta;
        if (dificultad.equalsIgnoreCase("fácil")) {
            segundosPorPregunta = 5;
        } else if (dificultad.equalsIgnoreCase("medio")) {
            segundosPorPregunta = 7;
        } else {
            segundosPorPregunta = 10;
        }

        tiempoTotal = cantidad * segundosPorPregunta;

        // Inicia el contador
        iniciarContador();


        binding.btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarRespuesta();
                siguientePregunta();
            }
        });

    }

    private void iniciarContador() {
        contadorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (tiempoTotal > 0 && sigueCuenta) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    tiempoTotal--;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int minutos = tiempoTotal / 60;
                            int segundos = tiempoTotal % 60;
                            String tiempoFormateado = String.format("%02d:%02d", minutos, segundos);
                            binding.textContador.setText(tiempoFormateado);

                            if (tiempoTotal == 0) {
                                sigueCuenta = false; // el hilo se detiene
                                binding.btnSiguiente.setEnabled(false); // Y aquí se desactiva el botón

                                Intent intent = new Intent(TeletriviaActivity.this, ReviewActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
        contadorThread.start();
    }


    private void siguientePregunta() {
        posicionPregunta++;
        if (posicionPregunta < listaPreguntas.size()) {
            preguntaActual();
        } else {
            sigueCuenta = false;
            binding.btnSiguiente.setEnabled(false);

            Intent intent = new Intent(TeletriviaActivity.this, ReviewActivity.class);
            intent.putExtra("correctas", correctas);
            intent.putExtra("incorrectas", incorrectas);
            intent.putExtra("noRespondidas", enBlanco);
            startActivity(intent);
            finish();

        }
    }


    private void obtenerPreguntas(int cantidad, String dificultad) {
        Call<Respuestas> call = apiService.obtenerPreguntas(cantidad, dificultad.toLowerCase(), "multiple");

        call.enqueue(new Callback<Respuestas>() {
            @Override
            public void onResponse(Call<Respuestas> call, Response<Respuestas> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listaPreguntas = response.body().getResults();
                    if (!listaPreguntas.isEmpty()) {
                        preguntaActual();
                    }
                }
            }

            @Override
            public void onFailure(Call<Respuestas> call, Throwable t) {
                Toast.makeText(TeletriviaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void preguntaActual() {
        if (posicionPregunta < listaPreguntas.size()) {
            Preguntas pregunta = listaPreguntas.get(posicionPregunta);

            // Mostrar la pregunta
            binding.textPregunta.setText(pregunta.getQuestion());

            List<String> opciones = new ArrayList<>(pregunta.getIncorrectAnswers());
            opciones.add(pregunta.getCorrectAnswer());

            // Para que las opciones se mezclen
            java.util.Collections.shuffle(opciones);

            // Las opciones
            binding.radioButton1.setText(opciones.get(0));
            binding.radioButton2.setText(opciones.get(1));
            binding.radioButton3.setText(opciones.get(2));
            binding.radioButton4.setText(opciones.get(3));

            // Para limpiar el radio button
            binding.radioGroupOpciones.clearCheck();
        }
    }

    private void validarRespuesta() {
        int idSeleccionado = binding.radioGroupOpciones.getCheckedRadioButtonId();

        if (idSeleccionado == -1) {
            enBlanco++;
            return;
        }

        String respuestaSeleccionada = "";

        if (idSeleccionado == binding.radioButton1.getId()) {
            respuestaSeleccionada = binding.radioButton1.getText().toString();
        } else if (idSeleccionado == binding.radioButton2.getId()) {
            respuestaSeleccionada = binding.radioButton2.getText().toString();
        } else if (idSeleccionado == binding.radioButton3.getId()) {
            respuestaSeleccionada = binding.radioButton3.getText().toString();
        } else if (idSeleccionado == binding.radioButton4.getId()) {
            respuestaSeleccionada = binding.radioButton4.getText().toString();
        }

        String respuestaCorrecta = listaPreguntas.get(posicionPregunta).getCorrectAnswer();

        if (respuestaSeleccionada.equalsIgnoreCase(respuestaCorrecta)) {
            correctas++;
        } else {
            incorrectas++;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sigueCuenta = false;
    }
}