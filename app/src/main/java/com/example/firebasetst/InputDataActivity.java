package com.example.firebasetst;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class InputDataActivity extends AppCompatActivity {

    private TextView titleTextView, locationTextView;
    private EditText descriptionEditText;
    private Button submitButton;
    private RatingBar ratingBar;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String userName;
    private String userId;
    private String locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        titleTextView = findViewById(R.id.titleTextView);
        locationTextView = findViewById(R.id.locationTextView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        submitButton = findViewById(R.id.submitButton);
        ratingBar = findViewById(R.id.ratingBar);

        // Get location data passed from LocationDetailsActivity
        String title = getIntent().getStringExtra("title");
        String location = getIntent().getStringExtra("location");
        locationId = getIntent().getStringExtra("id");  // Get the location ID

        // Ensure that data is not null
        if (title != null && location != null && locationId != null) {
            // Set the title and location on the TextViews
            titleTextView.setText(title);
            locationTextView.setText(location);
        } else {
            Toast.makeText(InputDataActivity.this, "Invalid data passed", Toast.LENGTH_SHORT).show();
        }

        // Fetch user information from Firebase Firestore (to get the user's name and user_id)
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loadUserData(user.getUid());
        } else {
            Toast.makeText(InputDataActivity.this, "User is not authenticated", Toast.LENGTH_SHORT).show();
        }

        submitButton.setOnClickListener(v -> {
            // Get the description entered by the user
            String description = descriptionEditText.getText().toString().trim();

            if (description.isEmpty()) {
                Toast.makeText(InputDataActivity.this, "Please enter a description", Toast.LENGTH_SHORT).show();
            } else if (userName != null) { // Ensure user name is fetched
                // Get the rating value
                float rating = ratingBar.getRating();

                // Call method to send the review to Firestore
                submitReviewToFirestore(title, location, description, userName, userId, rating);
            }
        });
    }

    private void loadUserData(String userId) {
        // Fetch user name and user_id from Firestore
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                userName = documentSnapshot.getString("name"); // Assuming "name" is the field in Firestore
                this.userId = documentSnapshot.getString("user_id"); // Retrieve the user_id from Firestore
            } else {
                Toast.makeText(InputDataActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(InputDataActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void submitReviewToFirestore(String title, String location, String description, String userName, String userId, float rating) {
        // Create Review object with location ID, user ID, and other details
        Review review = new Review(title, location, description, userName, userId, locationId, rating);

        // Add review to Firestore
        db.collection("reviews")
                .add(review)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(InputDataActivity.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(InputDataActivity.this, "Error submitting review: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}