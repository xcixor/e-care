package com.ecareuae.e_care;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvRegisterLink = findViewById(R.id.tv_register);
        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registrationPageIntent = new Intent(view.getContext(), UserTypeSelectionActivity.class);
                startActivity(registrationPageIntent);
            }
        });
    }
}

