package com.example.gasolinerasapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comunidad implements Serializable {
    @SerializedName("IDCCAA")
    private String idComunidad;

    @SerializedName("CCAA")
    private String comunidad;

    public String getIdComunidad() {
        return idComunidad;
    }

    public String getComunidad() {
        return comunidad;
    }

    @Override
    public String toString() {
        return "Comunidad{" +
                "idComunidad='" + idComunidad + '\'' +
                ", comunidad='" + comunidad + '\'' +
                '}';
    }
}
