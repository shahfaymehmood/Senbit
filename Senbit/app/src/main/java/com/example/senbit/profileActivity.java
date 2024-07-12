package com.example.senbit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

public class profileActivity extends AppCompatActivity {

    private static final String TAG = "profileActivity";

    private ImageView profileImage;
    private TextView username;
    private TextView email;
    private ImageView rewardImage;
    private TextView rewardPoints;
    private LinearLayout postedCodesContainer;
    private ImageButton homeButton;
    private ImageButton uploadButton;
    private ImageButton profileButton;
    private ImageButton editImage;
    private StringRequest mStringRequest;
    private RequestQueue mRequestQueue;
    private RequestQueue requestQueue;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            profileImage = findViewById(R.id.profileImage);
            username = findViewById(R.id.username);
            email = findViewById(R.id.email);
            rewardImage = findViewById(R.id.rewardImage);
            rewardPoints = findViewById(R.id.rewardPoints);
            postedCodesContainer = findViewById(R.id.postedCodesContainer);
            editImage = findViewById(R.id.editImage);

            homeButton = findViewById(R.id.homeButton);
            uploadButton = findViewById(R.id.uploadButton);
            profileButton = findViewById(R.id.profileButton);

            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            userId = sharedPreferences.getLong("userId", -1); // Default to -1 if not found

            if (userId == -1) {
                Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(profileActivity.this, Login.class);
                startActivity(intent);
                finish();
                return;
            }

            requestQueue = Volley.newRequestQueue(this);
            fetchUserProfileData(userId);
            fetchUserPostedCodes(userId);

            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(profileActivity.this, MainActivity.class));
                }
            });

            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(profileActivity.this, UploadActivity.class));
                }
            });

            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(profileActivity.this, ProfileBarActivity.class));
                }
            });

            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(profileActivity.this, editActivity.class));
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            Toast.makeText(this, "An error occurred during initialization", Toast.LENGTH_LONG).show();
        }
    }

    private void fetchUserProfileData(Long userId) {
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://senbit-backend.onrender.com/25435658/api/v1/bits/user/" + userId;

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject snippetObject = dataArray.getJSONObject(i);
                        if (snippetObject.has("author")) {
                            JSONObject authorArray = snippetObject.getJSONObject("author");
                            String fname = authorArray.optString("fname", "N/A");
                            String lname = authorArray.optString("lname", "N/A");
                            String emailStr = authorArray.optString("email", "N/A");
                            int points = authorArray.optInt("points", 0);

                            username.setText(fname + " " + lname);
                            email.setText(emailStr);
                            rewardPoints.setText(points + " points");
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Error: ", e);
                    Toast.makeText(profileActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error: ", error);
                Toast.makeText(profileActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void fetchUserPostedCodes(Long userId) {
        String url = "https://senbit-backend.onrender.com/25435658/api/v1/bits/user/" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");

                            if (status == 200) {
                                JSONArray dataArray = response.getJSONArray("data");
                                if (dataArray.length() == 0) {
                                    Toast.makeText(profileActivity.this, "No posted codes to show", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                postedCodesContainer.removeAllViews();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject codeObject = dataArray.getJSONObject(i);
                                    String title = codeObject.getString("title");
                                    String description = codeObject.getString("description");
                                    String content = codeObject.getString("content");
                                    String programmingLanguage = codeObject.getString("programmingLanguage");

                                    TextView codeTextView = new TextView(profileActivity.this);
                                    codeTextView.setText("Title: " + title + "\nDescription: " + description + "\nLanguage: " + programmingLanguage + "\nContent: " + content);
                                    codeTextView.setTextSize(14);
                                    codeTextView.setTextColor(getResources().getColor(R.color.white));
                                    codeTextView.setPadding(10, 100, 10, 100);
                                    codeTextView.setBackgroundResource(R.drawable.gradient_background);
                                    postedCodesContainer.addView(codeTextView);
                                }
                            } else {
                                String message = response.getString("message");
                                Toast.makeText(profileActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON Error: ", e);
                            Toast.makeText(profileActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error: ", error);
                Toast.makeText(profileActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
