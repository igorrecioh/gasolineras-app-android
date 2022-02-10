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
