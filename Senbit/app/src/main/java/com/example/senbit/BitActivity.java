package com.example.senbit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

public class BitActivity extends AppCompatActivity {

    private TextView codeTitle, codeOwner, codeDescription, codeLanguage, codeContent;
    private ImageButton upvoteButton, downvoteButton, saveButton;
    private Button postCommentButton;
    private EditText commentEditText;
    private LinearLayout commentsLayout;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Long userId;
    private int bitId;
    private int likedBy;
    private int dislikedBy;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit);
        Intent intent = getIntent();
        String programmingLanguage = intent.getStringExtra("programmingLanguage");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String owner = intent.getStringExtra("owner");
        String content = intent.getStringExtra("content");
        likedBy = intent.getIntExtra("likedBy",0);
        dislikedBy = intent.getIntExtra("dislikedBy",0);
        bitId = intent.getIntExtra("id",-1);

        codeTitle = findViewById(R.id.codeTitle);
        codeOwner = findViewById(R.id.codeOwner);
        codeDescription = findViewById(R.id.codeDescription);
        codeLanguage = findViewById(R.id.codeLanguage);
        codeContent = findViewById(R.id.codeContent);
        upvoteButton = findViewById(R.id.upvoteButton);
        downvoteButton = findViewById(R.id.downvoteButton);
        saveButton = findViewById(R.id.saveButton);
        postCommentButton = findViewById(R.id.postCommentButton);
        commentEditText = findViewById(R.id.commentEditText);
        commentsLayout = findViewById(R.id.commentsLayout);

        codeTitle.setText(title);
        codeOwner.setText(owner);
        codeDescription.setText(description);
        codeLanguage.setText("Language: " + programmingLanguage);
        codeContent.setText(content);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1); // Default to -1 if not found
        if (userId == -1 || bitId == -1) {
            Log.d("BIT", userId.toString() + " " + bitId);
            Toast.makeText(this, "User ID or Bit ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeDisLike(userId, bitId, "LIKE");
            }
        });

        downvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeDisLike(userId, bitId, "DISLIKE");
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BIT",String.valueOf(bitId));
                addCodeSnippet(userId, bitId);
            }
        });

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEditText.getText().toString().trim();
                if (!comment.isEmpty()) {
                    addComment(comment);
                    commentEditText.setText("");
                } else {
                    Toast.makeText(BitActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton uploadButton = findViewById(R.id.uploadButton);
        ImageButton profileButton = findViewById(R.id.profileButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BitActivity.this, MainActivity.class));
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BitActivity.this, UploadActivity.class));
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BitActivity.this, ProfileBarActivity.class));
            }
        });

        loadComments();
    }
    private void addCodeSnippet(Long userId, int bitId) {
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://senbit-backend.onrender.com/25435658/api/v1/collections/" + userId + "/" + bitId;
        mStringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status  = jsonObject.getInt("status");
                    if(status == 200){
                        Toast.makeText(BitActivity.this, "Saved to Collection!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("ABUZAR", "ERROR" + e.toString());
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

    private void likeDisLike(Long userId, int bitId, String action) {
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://senbit-backend.onrender.com/25435658/api/v1/bits/like/" + bitId + "?userId=" + userId + "&action=" + action.toUpperCase() ;
        mStringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status  = jsonObject.getInt("status");
                    if(status == 200){
                        if(action == "LIKE"){
//                            likedBy += 1;
                            Toast.makeText(BitActivity.this, "Upvoted!" + String.valueOf(likedBy), Toast.LENGTH_SHORT).show();
                        }
                        else {
//                            dislikedBy += 1;
                            Toast.makeText(BitActivity.this, "Downvoted!" + String.valueOf(dislikedBy), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    Log.d("ABUZAR", "ERROR" + e.toString());
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

    private void loadComments() {
        addComment("Nice work.");
        addComment("How can we do this in C language?");
        addComment("Really saved my time, thank you!");
    }

    private void addComment(String comment) {
        View commentView = getLayoutInflater().inflate(R.layout.commentlayout, null);
        TextView commentText = commentView.findViewById(R.id.commentText);
        ImageView commentPicture = commentView.findViewById(R.id.commentprofile);

        commentText.setText(comment);

       commentPicture.setImageResource(R.drawable.commentprofile);

        commentsLayout.addView(commentView);
    }
}
