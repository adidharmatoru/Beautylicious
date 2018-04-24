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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PembelianObatActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {
    private Spinner spinnerObat;
    private ArrayList<String> obat;
    private ArrayList<String> harga;
    private ArrayList<Integer> jumlah;
    SharedPreferences shared;
    private JSONArray result;
    public static final String MyPREFERENCES = "MyPrefs";
    private TextView tvHarga;
    private Spinner spinnerJumlah;
    private Integer i;
    private Button bBuy;
    private TextView tvOrderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian_obat);
        shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        spinnerObat = (Spinner) findViewById(R.id.spinnerObat);
        spinnerJumlah = (Spinner) findViewById(R.id.spinnerJumlah);
        tvOrderID = (TextView) findViewById(R.id.tvOrderID);
        obat = new ArrayList<>();
        harga = new ArrayList<>();
        jumlah = new ArrayList<Integer>();
        for (i = 1; i <= 10 ; i++) {
            jumlah.add(i);
        }
        tvHarga = (TextView) findViewById(R.id.tvHarga);
        bBuy = (Button) findViewById(R.id.bBuy);

        getData();
        spinnerJumlah.setAdapter(new ArrayAdapter<Integer>(PembelianObatActivity.this, android.R.layout.simple_spinner_dropdown_item, jumlah));
        spinnerObat.setOnItemSelectedListener(this);
        spinnerJumlah.setOnItemSelectedListener(this);

        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = shared.getString("userKey",null).toString();
                final int jumlah2 = Integer.parseInt(harga.get(0)) * Integer.parseInt(spinnerJumlah.getSelectedItem().toString());
                final String jumlah = Integer.toString(jumlah2);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String id = jsonResponse.getString("ID");
                            if (success) {
                                tvOrderID.setText("No Reservasi : " + id);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PembelianObatActivity.this);
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
                OrderRequest orderRequest = new OrderRequest(email, jumlah, responseListener);
                RequestQueue queue = Volley.newRequestQueue(PembelianObatActivity.this);
                queue.add(orderRequest);
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
                            getObat(result);
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
    private void getObat(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                obat.add(json.getString(SpinnerRequest.obat));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerObat.setAdapter(new ArrayAdapter<String>(PembelianObatActivity.this, android.R.layout.simple_spinner_dropdown_item, obat));
    }
    private void getHarga(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                harga.add(json.getString(spinnerObat.getSelectedItem().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        harga.clear();
        getHarga(result);
        int price = Integer.parseInt(harga.get(0));
        int total = price * Integer.parseInt(spinnerJumlah.getSelectedItem().toString());
        tvHarga.setText("Total : Rp" + NumberFormat.getNumberInstance(Locale.GERMAN).format(total));
    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        harga.clear();
    }
}
