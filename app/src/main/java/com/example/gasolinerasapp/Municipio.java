package com.example.gasolinerasapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Municipio implements Serializable {

    @SerializedName("IDMunicipio")
    private String idMunicipio;

    @SerializedName("IDProvincia")
    private String idProvincia;

    @SerializedName("IDCCAA")
    private String idComunidad;

    @SerializedName("Municipio")
    private String municipio;

    @SerializedName("Provincia")
    private String provincia;

    @SerializedName("CCAA")
    private String comunidad;

    public String getIdMunicipio() {
        return idMunicipio;
    }

    public String getIdProvincia() {
        return idProvincia;
    }

    public String getIdComunidad() {
        return idComunidad;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getComunidad() {
        return comunidad;
    }

    @Override
    public String toString() {
        return "Municipio{" +
                "idMunicipio='" + idMunicipio + '\'' +
                ", idProvincia='" + idProvincia + '\'' +
                ", idComunidad='" + idComunidad + '\'' +
                ", municipio='" + municipio + '\'' +
                ", provincia='" + provincia + '\'' +
                ", comunidad='" + comunidad + '\'' +
                '}';
    }
}
