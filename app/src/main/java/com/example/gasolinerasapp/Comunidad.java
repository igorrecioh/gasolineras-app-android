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
