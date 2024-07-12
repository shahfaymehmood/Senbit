package com.example.senbit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CodeSnippetAdapter adapter;
    private List<CodeSnippet> codeSnippetList;
    private List<CodeSnippet> filteredList;
    private CodeSnippet snippet;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private String url = "https://senbit-backend.onrender.com/25435658/api/v1/bits";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.searchView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        codeSnippetList = new ArrayList<>();
        filteredList = new ArrayList<>();
        loadCodeSnippets();

        adapter = new CodeSnippetAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        ImageButton uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UploadActivity.class));
            }
        });

        ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileBarActivity.class));
            }
        });
    }

    private void loadCodeSnippets() {
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    Log.d(TAG, "JSON OBJECT:" + dataArray.toString());

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject snippetObject = dataArray.getJSONObject(i);
                        int id = snippetObject.getInt("id");
                        String title = snippetObject.getString("title");
                        String description = snippetObject.getString("description");
                        String content = snippetObject.getString("content");
                        String programmingLanguage = snippetObject.getString("programmingLanguage");

                        // Extracting author information
                        JSONObject authorArray = snippetObject.getJSONObject("author");
                        String fname = authorArray.getString("fname");
                        String postedBy = "Posted By: " + fname;

                        // Extracting likedBy and dislikedBy lengths
//                        JSONArray likedByArray = snippetObject.getJSONArray("likedBy");
//                        JSONArray dislikedByArray = snippetObject.getJSONArray("dislikedBy");
//                        int likedByCount = likedByArray.length();
//                        int dislikedByCount = dislikedByArray.length();

                        // Creating a CodeSnippet object
                        snippet = new CodeSnippet(title, postedBy, description);
                        snippet.setProgrammingLanguage(programmingLanguage);
                        snippet.setContent(content);
                        snippet.setId(id);
//                        snippet.setLikedBy(likedByCount);
//                        snippet.setDisLikedBy(dislikedByCount);

                        // Adding the snippet to the list
                        codeSnippetList.add(snippet);
                    }

                    filteredList.addAll(codeSnippetList);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d(TAG, "ERROR: " + e.toString());
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

    @SuppressLint("NotifyDataSetChanged")
    private void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(codeSnippetList);
        } else {
            for (CodeSnippet snippet : codeSnippetList) {
                if (snippet.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(snippet);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
