package com.example.gasolinerasapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Provincia implements Serializable {

    @SerializedName("IDPovincia")
    private String idProvincia;

    @SerializedName("IDCCAA")
    private String idComunidad;

    @SerializedName("Provincia")
    private String provincia;

    @SerializedName("CCAA")
    private String comunidad;

    public String getIdProvincia() {
        return idProvincia;
    }

    public String getIdComunidad() {
        return idComunidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getComunidad() {
        return comunidad;
    }

    @Override
    public String toString() {
        return "Provincia{" +
                "idProvincia='" + idProvincia + '\'' +
                ", idComunidad='" + idComunidad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", comunidad='" + comunidad + '\'' +
                '}';
    }
}
