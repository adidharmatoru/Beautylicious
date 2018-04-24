package com.example.adidharmatorutama.beautylicious;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final Button bForget = (Button) findViewById(R.id.bForget);
    }
}
