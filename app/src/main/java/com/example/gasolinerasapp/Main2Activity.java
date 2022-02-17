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

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private List<Gasolinera> gasolineraList = new ArrayList<>();
    private List<Gasolinera> gasolineraListFiltered = new ArrayList<>();
    private RecyclerView recyclerView;
    private GasolineraAdapter gAdapter;
    private String selectedCombustible = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recycler_view);
        lookForGasolineras();

        gAdapter = new GasolineraAdapter(gasolineraListFiltered);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void lookForGasolineras() {
        gasolineraListFiltered.clear();
        gasolineraList.clear();
        gasolineraList = Arrays.asList((Gasolinera[]) getIntent().getSerializableExtra("GASOLINERAS"));
        selectedCombustible = (String) getIntent().getSerializableExtra("COMBUSTIBLE");

        if (!selectedCombustible.isEmpty()) {

            Gasolinera.sort(gasolineraList, selectedCombustible);

            switch (selectedCombustible) {
                case "Gasolina 95":
                    for (Gasolinera gasolinera : gasolineraList) {
                        if (!gasolinera.getGasolina95E5().trim().isEmpty()) {
                            gasolineraListFiltered.add(gasolinera);
                        }
                    }
                    break;

                case "Gasolina 98":
                    for (Gasolinera gasolinera : gasolineraList) {
                        if (!gasolinera.getGasolina98E5().trim().isEmpty()) {
                            gasolineraListFiltered.add(gasolinera);
                        }
                    }
                    break;

                case "Gasóleo A":
                    for (Gasolinera gasolinera : gasolineraList) {
                        if (!gasolinera.getGasA().trim().isEmpty()) {
                            gasolineraListFiltered.add(gasolinera);
                        }
                    }
                    break;

                case "Gasóleo Premium":
                    for (Gasolinera gasolinera : gasolineraList) {
                        if (!gasolinera.getGasPrem().trim().isEmpty()) {
                            gasolineraListFiltered.add(gasolinera);
                        }
                    }
                    break;

                default:
                    gasolineraListFiltered = gasolineraList;
                    break;
            }

        } else {
            gasolineraListFiltered = gasolineraList;
        }
    }
}
