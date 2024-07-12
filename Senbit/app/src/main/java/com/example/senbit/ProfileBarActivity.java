package com.example.senbit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileBarActivity extends AppCompatActivity {

    private TextView profileName;
    private TextView profileEmail;
    private TextView rewardPoints;

    private Long userId;

    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_bar);

        Button logoutButton = findViewById(R.id.logoutButton);
        LinearLayout profileLayout = findViewById(R.id.profileLayout);
        LinearLayout changePasswordLayout = findViewById(R.id.changePasswordLayout);
        LinearLayout collectionLayout = findViewById(R.id.collectionLayout);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        rewardPoints = findViewById(R.id.rewardPoints);


        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1); // Default to -1 if not found
        if (userId == -1) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(ProfileBarActivity.this,Login.class);
            return;
        }
        fetchUserProfileData(userId);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
            }
        });

        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangeLoginDetails();
            }
        });

        collectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCollection();
            }
        });
    }
    private void fetchUserProfileData(Long userId) {
        mRequestQueue = Volley.newRequestQueue(this);
        Log.d("PROFILE", "JSON OBJECT:");


        String url = "https://senbit-backend.onrender.com/25435658/api/v1/bits/user/" + userId;
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    Log.d("PROFILE", "JSON OBJECT:" + dataArray.toString());

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject snippetObject = dataArray.getJSONObject(i);
                        JSONObject authorArray = snippetObject.getJSONObject("author");
                        String fname = authorArray.getString("fname");
                        String lname = authorArray.getString("lname");
                        String email = authorArray.getString("email");
                        int points = authorArray.getInt("points");

                        Log.d("PROFILE", "fname:" + fname);
                        Log.d("PROFILE", "lname:" + lname);
                        profileName.setText(fname + " " + lname);
                        profileEmail.setText(email);
                        rewardPoints.setText(points + " points");
                    }

                } catch (JSONException e) {
                    Log.d("PROFILE", "ERROR: " + e.toString());
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Response: " + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Response: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(ProfileBarActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void goToProfile() {
        Intent intent = new Intent(ProfileBarActivity.this, profileActivity.class);
        startActivity(intent);
    }

    private void goToChangeLoginDetails() {
        Intent intent = new Intent(ProfileBarActivity.this, ChangeLoginDetailsActivity.class);
        startActivity(intent);
    }

    private void goToCollection() {
        Intent intent = new Intent(ProfileBarActivity.this, collectionActivity.class);
        startActivity(intent);
    }
}
