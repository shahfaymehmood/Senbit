package com.example.senbit;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class CreateAccount extends AppCompatActivity {

    private EditText editTextNewEmail;
    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextFirstname;
    private EditText editTextLastname;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Log.d("HERE", "onCreate:");
        editTextNewEmail = findViewById(R.id.editTextNewEmail);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextFirstname = findViewById(R.id.editTextNewFirstName);
        editTextLastname = findViewById(R.id.editTextNewLastName);
        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String email = editTextNewEmail.getText().toString().trim();
        String password = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String firstname = editTextFirstname.getText().toString().trim();
        String lastname = editTextLastname.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccountOnServer(email, password, firstname, lastname);
    }

    private void createAccountOnServer(final String email, final String password, final String fname, final String lname) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://senbit-backend.onrender.com/25435658/api/v1/users";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("fname", fname);
            jsonBody.put("lname", lname);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("VOLLEY", response);
                    Toast.makeText(CreateAccount.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", "Error: " + error.getMessage());
                    Toast.makeText(CreateAccount.this, "Account creation failed", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Error creating JSON body", Toast.LENGTH_SHORT).show();
        }
    }
}
