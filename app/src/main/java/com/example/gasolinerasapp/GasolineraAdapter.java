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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GasolineraAdapter extends RecyclerView.Adapter<GasolineraAdapter.MyViewHolder> {
    private final List<Gasolinera> gasolineras;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView rotulo, localidad, direccion, gasoleoA, gasoleoPrem, gasolina95, gasolina98;

        public MyViewHolder(View view) {
            super(view);
            rotulo = view.findViewById(R.id.rotulo);
            localidad = view.findViewById(R.id.localidad);
            direccion = view.findViewById(R.id.direccion);
            gasoleoA = view.findViewById(R.id.gasoleoA);
            gasoleoPrem = view.findViewById(R.id.gasoleoPrem);
            gasolina95 = view.findViewById(R.id.gasolina95);
            gasolina98 = view.findViewById(R.id.gasolina98);
        }
    }


    public GasolineraAdapter(List<Gasolinera> gasolineras) {
        this.gasolineras = gasolineras;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gasolinera_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Gasolinera gasolinera = gasolineras.get(position);
        holder.rotulo.setText(gasolinera.getRotulo());
        holder.localidad.setText(gasolinera.getCity());
        holder.direccion.setText(gasolinera.getStreet());
        holder.gasoleoA.setText(prettyPrintPrice(gasolinera.getGasA()));
        holder.gasoleoPrem.setText(prettyPrintPrice(gasolinera.getGasPrem()));
        holder.gasolina95.setText(prettyPrintPrice(gasolinera.getGasolina95E5()));
        holder.gasolina98.setText(prettyPrintPrice(gasolinera.getGasolina98E5()));
    }

    @Override
    public int getItemCount() {
        return gasolineras.size();
    }

    private String prettyPrintPrice(String precio) {
        if (!precio.isEmpty()) {
            return precio + " €";
        } else {
            return " - ";
        }
    }
}
