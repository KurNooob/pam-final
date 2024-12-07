package com.example.firebasetst;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LocationDetailsActivity extends AppCompatActivity {

    private String locationId, title, location;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private TextView titleTextView, locationTextView, overviewTextView;
    private EditText userReviewEditText;
    private RatingBar userRatingBar;
    private ImageView submitReviewButton, backButton, starButton;
    private Button staticButton;

    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList = new ArrayList<>();

    private String userId;
    private String userName; // Fetched from Firestore
    private boolean isFavorited = false; // Track favorite status
    private boolean isEditing = false; // Track edit mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get Intent data
        locationId = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        location = getIntent().getStringExtra("location");

        // Initialize UI elements
        backButton = findViewById(R.id.backButton);
        starButton = findViewById(R.id.starButton);
        submitReviewButton = findViewById(R.id.submitReviewButton);
        staticButton = findViewById(R.id.staticButton);

        titleTextView = findViewById(R.id.titleTextView);
        locationTextView = findViewById(R.id.locationTextView);
        overviewTextView = findViewById(R.id.overviewTextView);

        userReviewEditText = findViewById(R.id.userReviewEditText);
        userRatingBar = findViewById(R.id.userRatingBar);

        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(reviewList);
        reviewsRecyclerView.setAdapter(reviewAdapter);

        // Set location data
        titleTextView.setText(title);
        locationTextView.setText(location);
        overviewTextView.setText(getIntent().getStringExtra("overview"));

        userId = mAuth.getCurrentUser().getUid();

        // Fetch user details and prepopulate if the user has a review
        fetchUserDataAndReview();

        // Fetch and display all reviews for the location
        fetchAllReviews();

        // Check if the location is already favorited
        checkIfFavorited();

        // Back button functionality
        backButton.setOnClickListener(v -> finish());

        // Star button toggle functionality
        starButton.setOnClickListener(v -> toggleFavorite());

        // Submit review button listener for toggling edit and save modes
        submitReviewButton.setOnClickListener(v -> toggleEditMode());

        staticButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationDetailsActivity.this, ApiActivity.class);
            startActivity(intent);  // Start the ApiActivity
        });

        ImageView ivMainImage = findViewById(R.id.ivMainImage);

        // Handle click to open fullscreen activity
        ivMainImage.setOnClickListener(v -> {
            Intent intent = new Intent(LocationDetailsActivity.this, FullscreenActivity.class);

            // Pass image resource IDs and captions
            intent.putExtra("imageList", new int[]{
                    R.drawable.port1,  // Replace with your actual drawable names
                    R.drawable.port2  // Replace with your actual drawable names
            });
            intent.putExtra("captions", new String[]{
                    "Pantai Parangtritis - View 1",
                    "Pantai Parangtritis - View 2"
            });

            startActivity(intent);
        });
    }

    private void fetchUserDataAndReview() {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                userName = documentSnapshot.getString("name");

                // Fetch user's review for the location
                db.collection("reviews")
                        .document(userId + "_" + locationId)
                        .get()
                        .addOnSuccessListener(reviewSnapshot -> {
                            if (reviewSnapshot.exists()) {
                                Review review = reviewSnapshot.toObject(Review.class);
                                if (review != null) {
                                    userReviewEditText.setText(review.getDescription());
                                    userRatingBar.setRating(review.getRating());

                                    // Initially disable editing
                                    userReviewEditText.setEnabled(false);
                                    userRatingBar.setIsIndicator(true);
                                }
                            }
                        });
            }
        });
    }

    private void toggleEditMode() {
        if (isEditing) {
            // Save mode
            String userReview = userReviewEditText.getText().toString().trim();
            float userRating = userRatingBar.getRating();

            if (userReview.isEmpty()) {
                Toast.makeText(LocationDetailsActivity.this, "Please write a review!", Toast.LENGTH_SHORT).show();
                return;
            }

            submitOrUpdateReview(userReview, userRating);

            // Disable editing
            userReviewEditText.setEnabled(false);
            userRatingBar.setIsIndicator(true);
            submitReviewButton.setImageResource(R.drawable.pencil); // Change to edit icon
        } else {
            // Edit mode
            userReviewEditText.setEnabled(true);
            userRatingBar.setIsIndicator(false);
            submitReviewButton.setImageResource(R.drawable.send); // Change to save icon
        }

        isEditing = !isEditing; // Toggle the editing state
    }

    private void submitOrUpdateReview(String description, float rating) {
        if (userName == null || userName.isEmpty()) {
            Toast.makeText(this, "User data not loaded yet. Please wait.", Toast.LENGTH_SHORT).show();
            return;
        }

        Review review = new Review(title, location, description, userName, userId, locationId, rating);

        db.collection("reviews")
                .document(userId + "_" + locationId) // Unique document ID
                .set(review)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(LocationDetailsActivity.this, "Review saved successfully!", Toast.LENGTH_SHORT).show();
                    fetchAllReviews(); // Refresh the reviews list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LocationDetailsActivity.this, "Error saving review: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchAllReviews() {
        db.collection("reviews")
                .whereEqualTo("locationId", locationId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reviewList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Review review = document.toObject(Review.class);

                        // Avoid adding the user's review to the public reviews list
                        if (!review.getUserId().equals(userId)) {
                            reviewList.add(review);
                        }
                    }
                    reviewAdapter.notifyDataSetChanged();
                });
    }

    private void checkIfFavorited() {
        db.collection("favorites")
                .whereEqualTo("userId", userId)
                .whereEqualTo("locationId", locationId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    isFavorited = !queryDocumentSnapshots.isEmpty();
                    updateStarButtonUI();
                });
    }

    private void toggleFavorite() {
        if (isFavorited) {
            db.collection("favorites")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("locationId", locationId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            db.collection("favorites").document(document.getId()).delete();
                        }
                        isFavorited = false;
                        updateStarButtonUI();
                        Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Favorite favorite = new Favorite(userId, locationId);
            db.collection("favorites")
                    .add(favorite)
                    .addOnSuccessListener(documentReference -> {
                        isFavorited = true;
                        updateStarButtonUI();
                        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void updateStarButtonUI() {
        starButton.setImageResource(isFavorited ? R.drawable.ic_star_act : R.drawable.ic_star_inact);
    }
}
