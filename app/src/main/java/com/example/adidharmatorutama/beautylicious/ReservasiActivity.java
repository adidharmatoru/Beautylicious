package com.example.adidharmatorutama.beautylicious;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.*;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class ReservasiActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    private Spinner spinnerCabang;
    private ArrayList<String> cabang;
    private Spinner spinnerLayanan;
    private ArrayList<String> layanan;
    private Spinner spinnerJadwal;
    private ArrayList<String> jadwal;
    SharedPreferences shared;
    private JSONArray result;
    public static final String MyPREFERENCES = "MyPrefs";
    private TextView tvID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasi);
        shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        spinnerCabang = (Spinner) findViewById(R.id.spinnerCabang);
        spinnerLayanan = (Spinner) findViewById(R.id.spinnerLayanan);
        spinnerJadwal = (Spinner) findViewById(R.id.spinnerJadwal);
        cabang = new ArrayList<>();
        layanan = new ArrayList<>();
        jadwal = new ArrayList<>();
        Button bReserve = (Button) findViewById(R.id.bReserve);
        tvID = (TextView) findViewById(R.id.tvID);

        getData();
        spinnerCabang.setOnItemSelectedListener(this);

        bReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = shared.getString("userKey",null).toString();
                final String cabang = spinnerCabang.getSelectedItem().toString();
                final String layanan = spinnerLayanan.getSelectedItem().toString();
                final String jadwal = spinnerJadwal.getSelectedItem().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String id = jsonResponse.getString("ID");
                            if (success) {
                                tvID.setText("No Reservasi : " + id);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReservasiActivity.this);
                                builder.setMessage("Reserve Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ReserveRequest reserveRequest = new ReserveRequest(email, cabang, layanan, jadwal, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ReservasiActivity.this);
                queue.add(reserveRequest);
            }
        });
}
    private void getData(){
        StringRequest stringRequest = new StringRequest(SpinnerRequest.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(SpinnerRequest.JSON_ARRAY);
                            getCabang(result);
                            getLayanan(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void getCabang(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                cabang.add(json.getString(SpinnerRequest.branch));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerCabang.setAdapter(new ArrayAdapter<String>(ReservasiActivity.this, android.R.layout.simple_spinner_dropdown_item, cabang));
    }
    private void getLayanan(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                layanan.add(json.getString(SpinnerRequest.layanan));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerLayanan.setAdapter(new ArrayAdapter<String>(ReservasiActivity.this, android.R.layout.simple_spinner_dropdown_item, layanan));
    }
    private void getJadwal(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                jadwal.add(json.getString(spinnerCabang.getSelectedItem().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerJadwal.setAdapter(new ArrayAdapter<String>(ReservasiActivity.this, android.R.layout.simple_spinner_dropdown_item, jadwal));
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        jadwal.clear();
        getJadwal(result);
    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        jadwal.clear();
    }
}
