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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar myToolbar;
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

    private final Map<String, String> mapaCa = new HashMap<>();
    private final Map<String, String> mapaProv = new HashMap<>();
    private final Map<String, String> mapaMun = new HashMap<>();
    private final Map<String, String> mapaComb = new HashMap<>();

    private Context context;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;



        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // UI Elements initialization
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        comunidadSpinner = findViewById(R.id.comunidadSpinner);
        provinciaSpinner = findViewById(R.id.provinciaSpinner);
        provinciaSpinner.setEnabled(false);
        municipioSpinner = findViewById(R.id.municipioSpinner);
        municipioSpinner.setEnabled(false);
        combustiblesSpinner = findViewById(R.id.combustiblesSpinner);
        combustiblesSpinner.setEnabled(false);
        buscarBtn = findViewById(R.id.buscarBtn);
        buscarBtn.setEnabled(false);
        limpiarBusquedaBtn = findViewById(R.id.resetBtn);
        limpiarBusquedaBtn.setEnabled(false);
        cheapest = findViewById(R.id.cheapestSW);

        // Spinner adapters
        ArrayAdapter<String> adapterCa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterProv = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterMun = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterComb = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item);

        adapterCa.add(getString(R.string.sel_ca));
        adapterProv.add(getString(R.string.sel_prov));
        adapterMun.add(getString(R.string.sel_mun));
        adapterComb.add(getString(R.string.sel_comb));
        adapterComb.add(getString(R.string.gasoleoA));
        adapterComb.add(getString(R.string.gasoleoPrem));
        adapterComb.add(getString(R.string.gasolina95));
        adapterComb.add(getString(R.string.gasolina98));

        comunidadSpinner.setAdapter(adapterCa);
        provinciaSpinner.setAdapter(adapterProv);
        municipioSpinner.setAdapter(adapterMun);
        combustiblesSpinner.setAdapter(adapterComb);

        cheapest.setEnabled(false);

        //Creamos esta variable para guardar los valores seleccionados por el usuario al presionar Buscar, y para usarlos como valores iniciales en las siguientes ejecuciones de la aplicación
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        //Leemos el valor del Switch cheaper de los valores guardados, y cargamos dicho valor en el switch (si el valor no estaba guardado, se pone a False)
        //Si activamos el switch, también activamos el combustible.
        if(Boolean.parseBoolean(prefs.getString("CheapestSwitch", "false")))
        {
            cheapest.setChecked(true);
            combustiblesSpinner.setEnabled(true);
            combustiblesSpinner.setSelection(Integer.parseInt(prefs.getString("IndiceCombustible", "0")));

        }
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

            ArrayList<String> keyList = new ArrayList<>(mapaCa.keySet());
            String[] comunidadesArr = new String[keyList.size()];
            Collections.sort(keyList);
            comunidadesArr = keyList.toArray(comunidadesArr);
            adapterCa.addAll(comunidadesArr);
            comunidadSpinner.setAdapter(adapterCa);

            //Leemos el índice de la Comunidad seleccionada en ejecuciones anteriores, y la dejamos seleccionada en esta ejecución.
            comunidadSpinner.setSelection(Integer.parseInt(prefs.getString("IndiceComunidad", "0")));

            progressBar.setVisibility(View.GONE);
        }, error -> {
            Toast toast = Toast.makeText(this, getString(R.string.err_con), Toast.LENGTH_LONG);
            toast.show();
        });
        mRequestQueue.add(mJsonArrayRequest);

        // Spinners' management
        comunidadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!adapterView.getItemAtPosition(i).toString().equals(getString(R.string.sel_ca))) {
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
                        adapterProv.add(getString(R.string.sel_prov));
                        adapterProv.addAll(provinciasArr);
                        provinciaSpinner.setAdapter(adapterProv);

                        //Leemos el índice de la Provincia seleccionada en ejecuciones anteriores, y la dejamos seleccionada en esta ejecución (sólo si la Comunidad seleccionada anteriormente es la misma que en la ejecución anterior)
                        int comunidadSeleccionada = Integer.parseInt(String.valueOf(comunidadSpinner.getSelectedItemId()));
                        int comunidadAlmacenada = Integer.parseInt(prefs.getString("IndiceComunidad", "0"));
                        if (comunidadSeleccionada == comunidadAlmacenada){
                            provinciaSpinner.setSelection(Integer.parseInt(prefs.getString("IndiceProvincia", "0")));
                        }

                        //Si la Comunidad Autónoma sólo tiene una Provincia, dejamos ésta seleccionada.
                        if (provincias.length == 1) {
                            provinciaSpinner.setSelection(1);
                        }
                        progressBar.setVisibility(View.GONE);
                    }, error -> {
                        System.out.println("Error: " + error.toString());
                        Toast toast = Toast.makeText(context, getString(R.string.err_con), Toast.LENGTH_LONG);
                        toast.show();
                    });
                    mRequestQueue.add(mJsonArrayRequest);
                    provinciaSpinner.setEnabled(true);
                    limpiarBusquedaBtn.setEnabled(true);
                } else {
                    provinciaSpinner.setEnabled(false);
                    municipioSpinner.setEnabled(false);
                    provinciaSpinner.setSelection(0);
                    municipioSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        provinciaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).toString().equals(getString(R.string.sel_prov))) {
                    progressBar.setVisibility(View.VISIBLE);
                    municipioSpinner.setAdapter(null);
                    mapaMun.clear();
                    String munId = mapaProv.get(adapterView.getItemAtPosition(i).toString());
                    mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlMun + munId, null, response -> {
                        System.out.println("RMunic: " + response);
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
                        adapterMun.add(getString(R.string.sel_mun));
                        adapterMun.addAll(municipiosArr);
                        municipioSpinner.setAdapter(adapterMun);

                        //Leemos el índice del Municipio seleccionado en ejecuciones anteriores, y lo dejamos seleccionado en esta ejecución (sólo si la Provincia seleccionada es la misma que en la ejecución anterior)
                        int provinciaSeleccionada = Integer.parseInt(String.valueOf(provinciaSpinner.getSelectedItemId()));
                        int provinciaAlmacenada = Integer.parseInt(prefs.getString("IndiceProvincia", "0"));
                        if (provinciaSeleccionada == provinciaAlmacenada) {
                            municipioSpinner.setSelection(Integer.parseInt(prefs.getString("IndiceMunicipio", "0")));
                        }

                        //Si la Provincia seleccionada sólo tiene un Municipio, dejamos éste seleccionado.
                        if (municipios.length == 1) {
                            municipioSpinner.setSelection(1);
                        }
                        progressBar.setVisibility(View.GONE);
                    }, error -> {
                        Toast toast = Toast.makeText(context, getString(R.string.err_con), Toast.LENGTH_LONG);
                        toast.show();
                    });
                    mRequestQueue.add(mJsonArrayRequest);
                    municipioSpinner.setEnabled(true);
                } else {
                    municipioSpinner.setEnabled(false);
                    municipioSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        municipioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).toString().equals(getString(R.string.sel_mun))) {
                    progressBar.setVisibility(View.VISIBLE);
                    cheapest.setEnabled(true);
                    buscarBtn.setEnabled(true);
                    combustiblesSpinner.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else {
                    cheapest.setEnabled(false);
                    buscarBtn.setEnabled(false);
                    combustiblesSpinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        combustiblesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).toString().equals(getString(R.string.sel_comb))) {
                    selectedCombustible = adapterView.getItemAtPosition(i).toString();
                }
                else{
                    selectedCombustible = "";
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

            //Declaramos un objeto sharedPreferences para almacenar los valores seleccionados por el usuario
            SharedPreferences.Editor editor = prefs.edit();
            //Almacenamos los diferentes valores elegidos por el usuario para hacer la consulta
            editor.putString("IndiceComunidad", String.valueOf(comunidadSpinner.getSelectedItemPosition()));
            editor.putString("IndiceProvincia", String.valueOf(provinciaSpinner.getSelectedItemPosition()));
            editor.putString("IndiceMunicipio", String.valueOf(municipioSpinner.getSelectedItemPosition()));
            editor.putString("CheapestSwitch", String.valueOf(cheapest.isChecked()));
            editor.putString("IndiceCombustible", String.valueOf(combustiblesSpinner.getSelectedItemPosition()));
            editor.commit();


            mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlEstacionesMun + munId, null, response -> {
                try {
                    System.out.println(response.getJSONArray("ListaEESSPrecio"));
                    Gson gson = new Gson();
                    Gasolinera[] gasolineras = gson.fromJson(String.valueOf(response.getJSONArray("ListaEESSPrecio")), Gasolinera[].class);
                    List<Gasolinera> gasolinerasList = Arrays.asList(gasolineras);

                    Intent intent = new Intent(this, Main2Activity.class);
                    intent.putExtra("GASOLINERAS", gasolinerasList.toArray());
                    intent.putExtra("COMBUSTIBLE", selectedCombustible);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                System.out.println("Error: " + error.toString());
                Toast toast = Toast.makeText(context, getString(R.string.err_con), Toast.LENGTH_LONG);
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
        cheapest.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                combustiblesSpinner.setEnabled(true);
            } else {
                combustiblesSpinner.setSelection(0);
                combustiblesSpinner.setEnabled(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.action_settings:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("v0.0.3")
                        .setTitle("INFO");
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}