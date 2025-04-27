package com.example.lab03.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Respuestas {
    @SerializedName("response_code")
    private int responseCode;

    @SerializedName("results")
    private List<Preguntas> results;

    public int getResponseCode() {
        return responseCode;
    }

    public List<Preguntas> getResults() {
        return results;
    }
}