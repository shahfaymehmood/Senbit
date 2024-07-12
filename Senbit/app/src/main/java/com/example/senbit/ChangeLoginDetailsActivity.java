package com.example.senbit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ChangeLoginDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_login_details);

        Button saveButton = findViewById(R.id.buttonSaveChanges);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // logic to save user details

                Intent intent = new Intent(ChangeLoginDetailsActivity.this, ProfileBarActivity.class);
                startActivity(intent);
            }
        });
    }
}
