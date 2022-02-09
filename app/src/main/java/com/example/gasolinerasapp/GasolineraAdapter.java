package com.example.gasolinerasapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GasolineraAdapter extends RecyclerView.Adapter<GasolineraAdapter.MyViewHolder> {
    private List<Gasolinera> gasolineras;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView rotulo, localidad, gasoleoA, gasoleoPrem, gasolina95, gasolina98;

        public MyViewHolder(View view) {
            super(view);
            rotulo = (TextView) view.findViewById(R.id.rotulo);
            localidad = (TextView) view.findViewById(R.id.localidad);
            gasoleoA = (TextView) view.findViewById(R.id.gasoleoA);
            gasoleoPrem = (TextView) view.findViewById(R.id.gasoleoPrem);
            gasolina95 = (TextView) view.findViewById(R.id.gasolina95);
            gasolina98 = (TextView) view.findViewById(R.id.gasolina98);
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
