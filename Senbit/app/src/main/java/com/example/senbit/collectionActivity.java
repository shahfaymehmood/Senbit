package com.example.senbit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class collectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CodeAdapter codeAdapter;
    private List<Code> codeList;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Long userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        codeList = new ArrayList<>();
        codeAdapter = new CodeAdapter(codeList, new CodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Code code) {
                Intent intent = new Intent(collectionActivity.this, CodeAdapter.class);
                intent.putExtra("code", code);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(codeAdapter);
        loadSavedCodes();
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1); // Default to -1 if not found
        if (userId == -1) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }
        loadCollection(userId);


        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(collectionActivity.this, MainActivity.class));
            }
        });

        ImageButton uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(collectionActivity.this, UploadActivity.class));
            }
        });

        ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(collectionActivity.this, ProfileBarActivity.class));
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadSavedCodes() {
        codeAdapter.notifyDataSetChanged();
    }
    private void loadCollection(Long userId) {
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://senbit-backend.onrender.com/25435658/api/v1/collections/" + userId;
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray collectionsArray = dataObject.getJSONArray("collectionsList");
                    for (int i = 0; i < collectionsArray.length(); i++) {
                        JSONObject collectionObject = collectionsArray.getJSONObject(i);
                        String title = collectionObject.getString("title");
                        String description = collectionObject.getString("description");
                        String content = collectionObject.getString("content");
                        String programmingLanguage = collectionObject.getString("programmingLanguage");
                        JSONObject authorObject = collectionObject.getJSONObject("author");
                        String authorFname = authorObject.getString("fname");
                        String authorLname = authorObject.getString("lname");
                        Log.d("COLLECTION", "Title: " + title);
                        Log.d("COLLECTION", "Description: " + description);
                        Log.d("COLLECTION", "Content: " + content);
                        Log.d("COLLECTION", "Programming Language: " + programmingLanguage);
                        Log.d("COLLECTION", "Author: " + authorFname + " " + authorLname);
                        codeList.add(new Code(title, authorFname + " " + authorLname,description, programmingLanguage,content));
                    }
                    codeAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.d("COLLECTION", "ERROR: " + e.toString());
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Response: " + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Response: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

}
