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

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private Spinner comunidadSpinner;
    private Spinner provinciaSpinner;
    private Spinner municipioSpinner;
    private Spinner combustiblesSpinner;

    private Button buscarBtn;
    private Button limpiarBusquedaBtn;

    private Switch cheapest;

    private RequestQueue mRequestQueue;
    private JsonArrayRequest mJsonArrayRequest;
    private JsonObjectRequest mJsonObjectRequest;

    private String selectedCombustible = "";
    private final String url = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/Listados/ComunidadesAutonomas/";
    private final String urlProv = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/Listados/ProvinciasPorComunidad/";
    private final String urlMun = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/Listados/MunicipiosPorProvincia/";
    private final String urlEstacionesMun = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroMunicipio/";

    private Map<String, String> mapaCa = new HashMap<>();
    private Map<String, String> mapaProv = new HashMap<>();
    private Map<String, String> mapaMun = new HashMap<>();
    private Map<String, String> mapaComb = new HashMap<>();

    private boolean showCheapest = false;

    private Context context;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // UI Elements initialization
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        comunidadSpinner = (Spinner) findViewById(R.id.comunidadSpinner);
        provinciaSpinner = (Spinner) findViewById(R.id.provinciaSpinner);
        provinciaSpinner.setEnabled(false);
        municipioSpinner = (Spinner) findViewById(R.id.municipioSpinner);
        municipioSpinner.setEnabled(false);
        combustiblesSpinner = (Spinner) findViewById(R.id.combustiblesSpinner);
        combustiblesSpinner.setEnabled(false);
        buscarBtn = (Button) findViewById(R.id.buscarBtn);
        buscarBtn.setEnabled(false);
        limpiarBusquedaBtn = (Button) findViewById(R.id.resetBtn);
        limpiarBusquedaBtn.setEnabled(false);
        cheapest = (Switch) findViewById(R.id.cheapestSW);

        // Spinner adapters
        ArrayAdapter<String> adapterCa = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapterProv = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapterMun = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapterComb = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);

        adapterCa.add("Seleccione CA");
        adapterProv.add("Seleccione Provincia");
        adapterMun.add("Seleccione Municipio");
        adapterComb.add("Seleccione combustible");
        adapterComb.add(getString(R.string.gasoleoA));
        adapterComb.add(getString(R.string.gasoleoPrem));
        adapterComb.add(getString(R.string.gasolina95));
        adapterComb.add(getString(R.string.gasolina98));

        comunidadSpinner.setAdapter(adapterCa);
        provinciaSpinner.setAdapter(adapterProv);
        municipioSpinner.setAdapter(adapterMun);
        combustiblesSpinner.setAdapter(adapterComb);

        // Initial request for CCAA
        mRequestQueue = Volley.newRequestQueue(this);
        mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            progressBar.setVisibility(View.VISIBLE);
            System.out.println("RCom: " + response);
            Gson gson = new Gson();
            Comunidad[] comunidades = gson.fromJson(String.valueOf(response), Comunidad[].class);

            for (int i = 0; i < comunidades.length; i++) {
                mapaCa.put(comunidades[i].getComunidad(), comunidades[i].getIdComunidad());
            }

            ArrayList<String> keyList = new ArrayList<String>(mapaCa.keySet());
            String[] comunidadesArr = new String[keyList.size()];
            Collections.sort(keyList);
            comunidadesArr = keyList.toArray(comunidadesArr);
            adapterCa.addAll(comunidadesArr);
            comunidadSpinner.setAdapter(adapterCa);
            progressBar.setVisibility(View.GONE);
        }, error -> {
            // System.out.println("Error: " + error.toString());
            Toast toast = Toast.makeText(this, "Revisa tu conexión", Toast.LENGTH_LONG);
            toast.show();
        });
        mRequestQueue.add(mJsonArrayRequest);

        // Spinners' management
        comunidadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).toString().contains("Seleccione")) {
                    progressBar.setVisibility(View.VISIBLE);
                    provinciaSpinner.setAdapter(null);
                    mapaProv.clear();
                    String provId = mapaCa.get(adapterView.getItemAtPosition(i).toString());
                    mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlProv + provId, null, response -> {
                        System.out.println("RProv: " + response);
                        Gson gson = new Gson();
                        Provincia[] provincias = gson.fromJson(String.valueOf(response), Provincia[].class);

                        for (int j = 0; j < provincias.length; j++) {
                            mapaProv.put(provincias[j].getProvincia(), provincias[j].getIdProvincia());
                        }

                        ArrayList<String> keyList = new ArrayList<String>(mapaProv.keySet());
                        String[] provinciasArr = new String[keyList.size()];
                        Collections.sort(keyList);
                        provinciasArr = keyList.toArray(provinciasArr);
                        adapterProv.clear();
                        adapterProv.add("Seleccione Provincia");
                        adapterProv.addAll(provinciasArr);
                        provinciaSpinner.setAdapter(adapterProv);
                        progressBar.setVisibility(View.GONE);
                    }, error -> {
                        System.out.println("Error: " + error.toString());
                        Toast toast = Toast.makeText(context, "Revisa tu conexión", Toast.LENGTH_LONG);
                        toast.show();
                    });
                    mRequestQueue.add(mJsonArrayRequest);
                    provinciaSpinner.setEnabled(true);
                    limpiarBusquedaBtn.setEnabled(true);
                } else {
                    provinciaSpinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        provinciaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).toString().contains("Seleccione")) {
                    progressBar.setVisibility(View.VISIBLE);
                    municipioSpinner.setAdapter(null);
                    mapaMun.clear();
                    String munId = mapaProv.get(adapterView.getItemAtPosition(i).toString());
                    mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlMun + munId, null, response -> {
                        System.out.println("RProv: " + response);
                        Gson gson = new Gson();
                        Municipio[] municipios = gson.fromJson(String.valueOf(response), Municipio[].class);

                        for (int j = 0; j < municipios.length; j++) {
                            mapaMun.put(municipios[j].getMunicipio(), municipios[j].getIdMunicipio());
                        }

                        ArrayList<String> keyList = new ArrayList<String>(mapaMun.keySet());
                        String[] municipiosArr = new String[keyList.size()];
                        Collections.sort(keyList);
                        municipiosArr = keyList.toArray(municipiosArr);
                        adapterMun.clear();
                        adapterMun.add("Seleccione Municipio");
                        adapterMun.addAll(municipiosArr);
                        municipioSpinner.setAdapter(adapterMun);
                        progressBar.setVisibility(View.GONE);
                    }, error -> {
                        // System.out.println("Error: " + error.toString());
                        Toast toast = Toast.makeText(context, "Revisa tu conexión", Toast.LENGTH_LONG);
                        toast.show();
                    });
                    mRequestQueue.add(mJsonArrayRequest);
                    municipioSpinner.setEnabled(true);
                } else {
                    municipioSpinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        municipioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).toString().contains("Seleccione")) {
                    progressBar.setVisibility(View.VISIBLE);
                    buscarBtn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else {
                    buscarBtn.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        combustiblesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).toString().contains("Seleccione")) {
                    selectedCombustible = adapterView.getItemAtPosition(i).toString();
                    System.out.println("Combustible: " + selectedCombustible);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Buttons' management
        buscarBtn.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String munId = mapaMun.get(municipioSpinner.getSelectedItem().toString());
            mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlEstacionesMun + munId, null, response -> {
                try {
                    System.out.println(response.getJSONArray("ListaEESSPrecio"));
                    Gson gson = new Gson();
                    Gasolinera[] gasolineras = gson.fromJson(String.valueOf(response.getJSONArray("ListaEESSPrecio")), Gasolinera[].class);

                    List<Gasolinera> gasolinerasList = Arrays.asList(gasolineras);

                    for (Gasolinera gasolinera : gasolinerasList) {
                        System.out.println(gasolinera.toString());
                    }

                    Gasolinera.sort(gasolinerasList, selectedCombustible);

                    for (Gasolinera gasolinera : gasolinerasList) {
                        System.out.println(gasolinera.toString());
                    }

                    Intent intent = new Intent(this, Main2Activity.class);
                    intent.putExtra("GASOLINERAS", gasolinerasList.toArray());
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                System.out.println("Error: " + error.toString());
                Toast toast = Toast.makeText(context, "Revisa tu conexión", Toast.LENGTH_LONG);
                toast.show();
            });
            mRequestQueue.add(mJsonObjectRequest);
        });

        limpiarBusquedaBtn.setOnClickListener(view -> {
            comunidadSpinner.setSelection(0);
            provinciaSpinner.setSelection(0);
            provinciaSpinner.setEnabled(false);
            municipioSpinner.setSelection(0);
            municipioSpinner.setEnabled(false);
            buscarBtn.setEnabled(false);
            cheapest.setChecked(false);
        });

        // Switch management
        cheapest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    combustiblesSpinner.setEnabled(true);
                    showCheapest = true;
                } else {
                    combustiblesSpinner.setSelection(0);
                    combustiblesSpinner.setEnabled(false);
                    showCheapest = false;
                }
            }
        });
    }
}