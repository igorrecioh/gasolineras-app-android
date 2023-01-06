// GasolinerasApp - Aplicación Android para buscar gasolineras
// Copyright (C) 2022  Igor Recio
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package com.example.gasolinerasapp;

import android.content.Context;
import android.content.res.Resources;
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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

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
    public static List<Gasolinera> sort(List<Gasolinera> gasolineras, String by, Context context)
    {

        if (by.equals(context.getResources().getString(R.string.gasoleoA))) {
            gasolineras.sort(Comparator.comparing(Gasolinera::getGasA));
        } else if (by.equals(context.getResources().getString(R.string.gasoleoPrem))) {
            gasolineras.sort(Comparator.comparing(Gasolinera::getGasPrem));
        } else if (by.equals(context.getResources().getString(R.string.gasolina95))) {
            gasolineras.sort(Comparator.comparing(Gasolinera::getGasolina95E5));
        } else if (by.equals(context.getResources().getString(R.string.gasolina98))) {
            gasolineras.sort(Comparator.comparing(Gasolinera::getGasolina98E5));
        }
        return gasolineras;
    }
}
