package com.example.senbit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextCode;
    private Spinner langSpinner;
    private RequestQueue requestQueue;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCode = findViewById(R.id.editTextCode);
        langSpinner = findViewById(R.id.langSpinner);
        Button buttonUpload = findViewById(R.id.buttonUpload);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1); // Default to -1 if not found

        requestQueue = Volley.newRequestQueue(this);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCode();
            }
        });

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, ProfileBarActivity.class);
                startActivity(intent);
            }
        });
    }

    private void uploadCode() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String code = editTextCode.getText().toString().trim();
        String language = langSpinner.getSelectedItem().toString().trim();

        if (title.isEmpty() || description.isEmpty() || code.isEmpty() || language.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject bitsObject = new JSONObject();
        try {
            bitsObject.put("title", title);
            bitsObject.put("description", description);
            bitsObject.put("content", code);
            bitsObject.put("programmingLanguage", language);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (userId == -1) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://senbit-backend.onrender.com/25435658/api/v1/bits/new/" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, bitsObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Toast.makeText(UploadActivity.this, message, Toast.LENGTH_SHORT).show();
                            if (response.getInt("status") == 201) {
                                editTextTitle.setText("");
                                editTextDescription.setText("");
                                editTextCode.setText("");
                                langSpinner.setSelection(0);
                                Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SHAHFAY", "Account logged in successfully");
                Toast.makeText(UploadActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
