package com.example.gasolinerasapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Gasolinera implements Serializable {

    @SerializedName("C.P.")
    private String postCode;

    @SerializedName("Dirección")
    private String street;

    @SerializedName("Horario")
    private String timetable;

    @SerializedName("Latitud")
    private String latitude;

    @SerializedName("Localidad")
    private String city;

    @SerializedName("Longitud (WGS84)")
    private String longitude;

    @SerializedName("Margen")
    private String margin;

    @SerializedName("Precio Biodiesel")
    private String biodiesel;

    @SerializedName("Precio Bioetanol")
    private String bioetanol;

    @SerializedName("Precio Gas Natural Comprimido")
    private String gasNaturalComp;

    @SerializedName("Precio Gas Natural Licuado")
    private String gasNaturalLic;

    @SerializedName("Precio Gases licuados del petróleo")
    private String GasLicPet;

    @SerializedName("Precio Gasoleo A")
    private String gasA;

    @SerializedName("Precio Gasoleo B")
    private String gasB;

    @SerializedName("Precio Gasoleo Premium")
    private String gasPrem;

    @SerializedName("Precio Gasolina 95 E10")
    private String gasolina95E10;

    @SerializedName("Precio Gasolina 95 E5")
    private String gasolina95E5;

    @SerializedName("Precio Gasolina 95 E5 Premium")
    private String gasolina95E5Prem;

    @SerializedName("Precio Gasolina 98 E10")
    private String gasolina98E10;

    @SerializedName("Precio Gasolina 98 E5")
    private String gasolina98E5;

    @SerializedName("Precio Hidrogeno")
    private String hidrogeno;

    @SerializedName("Rótulo")
    private String rotulo;

    @SerializedName("Tipo Venta")
    private String venta;

    @SerializedName("% BioEtanol")
    private String bioetanolPercent;

    @SerializedName("% Éster metílico")
    private String esterMetPercent;


    public String getCity() {
        return city;
    }

    public String getRotulo() {
        return rotulo;
    }

    public String getGasA() {
        return gasA;
    }

    public String getGasPrem() {
        return gasPrem;
    }

    public String getGasolina95E5() {
        return gasolina95E5;
    }

    public String getGasolina98E5() {
        return gasolina98E5;
    }

    public String getStreet() { return street; }

    @Override
    public String toString() {
        return "Gasolinera{" +
                "postCode='" + postCode + '\'' +
                ", street='" + street + '\'' +
                ", timetable='" + timetable + '\'' +
                ", latitude='" + latitude + '\'' +
                ", city='" + city + '\'' +
                ", longitude='" + longitude + '\'' +
                ", margin='" + margin + '\'' +
                ", biodiesel='" + biodiesel + '\'' +
                ", bioetanol='" + bioetanol + '\'' +
                ", gasNaturalComp='" + gasNaturalComp + '\'' +
                ", gasNaturalLic='" + gasNaturalLic + '\'' +
                ", GasLicPet='" + GasLicPet + '\'' +
                ", gasA='" + gasA + '\'' +
                ", gasB='" + gasB + '\'' +
                ", gasPrem='" + gasPrem + '\'' +
                ", gasolina95E10='" + gasolina95E10 + '\'' +
                ", gasolina95E5='" + gasolina95E5 + '\'' +
                ", gasolina95E5Prem='" + gasolina95E5Prem + '\'' +
                ", gasolina98E10='" + gasolina98E10 + '\'' +
                ", gasolina98E5='" + gasolina98E5 + '\'' +
                ", hidrogeno='" + hidrogeno + '\'' +
                ", rotulo='" + rotulo + '\'' +
                ", venta='" + venta + '\'' +
                ", bioetanolPercent='" + bioetanolPercent + '\'' +
                ", esterMetPercent='" + esterMetPercent + '\'' +
                '}';
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Gasolinera> sort(List<Gasolinera> gasolineras, String by)
    {
        if (by.equals("Gasóleo A")) {
            gasolineras.sort(Comparator.comparing(Gasolinera::getGasA));
        } else if (by.equals("Gasóleo Premium")) {
            gasolineras.sort(Comparator.comparing(Gasolinera::getGasPrem));
        } else if (by.equals("Gasolina 95")) {
            gasolineras.sort(Comparator.comparing(Gasolinera::getGasolina95E5));
        } else if (by.equals("Gasolina 98")) {
            gasolineras.sort(Comparator.comparing(Gasolinera::getGasolina98E5));
        }
        return gasolineras;
    }
}
