package com.ecareuae.e_care;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

public class UserTypeSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_selection);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_doctor:
                if (checked) {
                    Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                    startActivity(intent);
                }
                    break;
            case R.id.radio_patient:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }
}
