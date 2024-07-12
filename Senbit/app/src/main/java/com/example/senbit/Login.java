package com.example.senbit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonSignIn = findViewById(R.id.buttonSignIn);
        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, CreateAccount.class);
                startActivity(intent);
            }
        });
    }

    private void signIn() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        authenticateUser(email, password);
    }

    private void authenticateUser(String email, String password) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://senbit-backend.onrender.com/25435658/api/v1/users/login";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("SHAHFAY", "Response:" + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int status = jsonResponse.getInt("status");

                        if (status == 200) {
                            Log.d("SHAHFAY", "Account logged in successfully");
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            // Save userId in SharedPreferences
                            JSONObject userData = jsonResponse.getJSONObject("data");
                            long userId = userData.getLong("id");
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong("userId", userId);
                            editor.apply();

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("VOLLEY", "Account creation failed: " + jsonResponse.getString("message"));
                            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Login.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", "Error: " + error.getMessage());
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
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
