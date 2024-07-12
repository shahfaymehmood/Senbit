package com.example.senbit;

import static com.example.senbit.R.*;
import static com.example.senbit.R.id.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Introduction extends AppCompatActivity {

    private final EditText introField = findViewById(id.introField);
    private final EditText domainField = findViewById(id.domainField);
    private final EditText orgField = findViewById(id.orgField);
    private final Spinner langSpinner = findViewById(id.langSpinner);
    private final Spinner expSpinner = findViewById(id.expSpinner);


    public Introduction() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button submitButton = findViewById(R.id.submitButton);

        ArrayAdapter<CharSequence> langAdapter = ArrayAdapter.createFromResource(this,
                R.array.Languages, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(langAdapter);

        ArrayAdapter<CharSequence> expAdapter = ArrayAdapter.createFromResource(this,
                R.array.experience, android.R.layout.simple_spinner_item);
        expAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expSpinner.setAdapter(expAdapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String introduction = introField.getText().toString();
                String domain = domainField.getText().toString();
                String organization = orgField.getText().toString();
                String language = langSpinner.getSelectedItem().toString();
                String experience = expSpinner.getSelectedItem().toString();

                String output = "Introduction: " + introduction + "\n"
                        + "Domain: " + domain + "\n"
                        + "Organization: " + organization + "\n"
                        + "Languages: " + language + "\n"
                        + "Experience: " + experience + " years\n";
            }
        });
    }
}