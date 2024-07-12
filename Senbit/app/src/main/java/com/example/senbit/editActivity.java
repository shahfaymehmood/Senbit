package com.example.senbit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class editActivity extends AppCompatActivity {

    private EditText editTextNewFirstName;
    private EditText editTextNewLastName;
    private EditText introField;
    private Spinner domainSpinner;
    private EditText orgField;
    private Spinner langSpinner;
    private Spinner expSpinner;

    private SharedPreferences sharedPreferences;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTextNewFirstName = findViewById(R.id.editTextNewFirstName);
        editTextNewLastName = findViewById(R.id.editTextNewLastName);
        introField = findViewById(R.id.introField);
        domainSpinner = findViewById(R.id.domainSpinner);
        orgField = findViewById(R.id.orgField);
        langSpinner = findViewById(R.id.langSpinner);
        expSpinner = findViewById(R.id.expSpinner);

        ArrayAdapter<CharSequence> domainAdapter = ArrayAdapter.createFromResource(this, R.array.domains, android.R.layout.simple_spinner_item);
        domainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        domainSpinner.setAdapter(domainAdapter);

        ArrayAdapter<CharSequence> langAdapter = ArrayAdapter.createFromResource(this, R.array.Languages, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(langAdapter);

        ArrayAdapter<CharSequence> expAdapter = ArrayAdapter.createFromResource(this, R.array.experience, android.R.layout.simple_spinner_item);
        expAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expSpinner.setAdapter(expAdapter);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1);

        populateFieldsFromAPI();

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void populateFieldsFromAPI() {
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String URL = "https://senbit-backend.onrender.com/25435658/api/v1/user/" + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("EditActivity", "Response: " + response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int status = jsonResponse.getInt("status");

                    if (status == 200) {
                        JSONObject userData = jsonResponse.getJSONObject("data");

                        String firstName = userData.getString("firstName");
                        String lastName = userData.getString("lastName");
                        String intro = userData.getString("intro");
                        String domain = userData.getString("domain");
                        String organization = userData.getString("organization");
                        String language = userData.getString("language");
                        int experience = userData.getInt("experience");

                        editTextNewFirstName.setText(firstName);
                        editTextNewLastName.setText(lastName);
                        introField.setText(intro);
                        orgField.setText(organization);

                        setSpinnerSelection(domainSpinner, R.array.domains, domain);
                        setSpinnerSelection(langSpinner, R.array.Languages, language);
                        setSpinnerSelection(expSpinner, R.array.experience, String.valueOf(experience));

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("firstName", firstName);
                        editor.putString("lastName", lastName);
                        editor.putString("intro", intro);
                        editor.putString("domain", domain);
                        editor.putString("organization", organization);
                        editor.putString("language", language);
                        editor.putInt("experience", experience);
                        editor.apply();

                    } else {
                        Toast.makeText(editActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(editActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EditActivity", "Error: " + error.getMessage());
                Toast.makeText(editActivity.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void setSpinnerSelection(Spinner spinner, int arrayResId, String value) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResId, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        if (value != null) {
            int position = adapter.getPosition(value);
            if (position >= 0) {
                spinner.setSelection(position);
            }
        }
    }

    private void saveProfile() {
        String URL = "https://senbit-backend.onrender.com/25435658/api/v1/user/" + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("firstName", editTextNewFirstName.getText().toString().trim());
            jsonBody.put("lastName", editTextNewLastName.getText().toString().trim());
            jsonBody.put("intro", introField.getText().toString().trim());
            jsonBody.put("domain", domainSpinner.getSelectedItem().toString());
            jsonBody.put("organization", orgField.getText().toString().trim());
            jsonBody.put("language", langSpinner.getSelectedItem().toString());
            jsonBody.put("experience", Integer.parseInt(expSpinner.getSelectedItem().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("EditActivity", "Response: " + response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int status = jsonResponse.getInt("status");

                    if (status == 200) {
                        Toast.makeText(editActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("firstName", editTextNewFirstName.getText().toString().trim());
                        editor.putString("lastName", editTextNewLastName.getText().toString().trim());
                        editor.putString("intro", introField.getText().toString().trim());
                        editor.putString("domain", domainSpinner.getSelectedItem().toString());
                        editor.putString("organization", orgField.getText().toString().trim());
                        editor.putString("language", langSpinner.getSelectedItem().toString());
                        editor.putInt("experience", Integer.parseInt(expSpinner.getSelectedItem().toString()));
                        editor.apply();

                    } else {
                        Toast.makeText(editActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(editActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EditActivity", "Error: " + error.getMessage());
                Toast.makeText(editActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public byte[] getBody() {
                return requestBody.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        requestQueue.add(stringRequest);
    }
}
