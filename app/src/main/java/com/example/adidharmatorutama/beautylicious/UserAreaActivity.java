package com.example.adidharmatorutama.beautylicious;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

public class UserAreaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");
        int age = intent.getIntExtra("age", -1);

        TextView tvWelcomeMsg = (TextView) findViewById(R.id.tvWelcomeMsg);
        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etAge = (EditText) findViewById(R.id.etAge);
        Button bObat = (Button) findViewById(R.id.bObat);
        Button bReservasi = (Button) findViewById(R.id.bReservasi);
        Button bKonsultasi = (Button) findViewById(R.id.bKonsultasi);
        etUsername.setFocusable(false);
        etAge.setFocusable(false);

        // Display user details
        String message = name + " welcome to your user area";
        tvWelcomeMsg.setText(message);
        etUsername.setText(username);
        etAge.setText(age + "");

        bReservasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reservasiIntent = new Intent(UserAreaActivity.this, ReservasiActivity.class);
                UserAreaActivity.this.startActivity(reservasiIntent);
            }
        });
        bKonsultasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent konsultasiIntent = new Intent(UserAreaActivity.this, KonsultasiActivity.class);
                UserAreaActivity.this.startActivity(konsultasiIntent);
            }
        });
        bObat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obatIntent = new Intent(UserAreaActivity.this, PembelianObatActivity.class);
                UserAreaActivity.this.startActivity(obatIntent);
            }
        });
    }
}