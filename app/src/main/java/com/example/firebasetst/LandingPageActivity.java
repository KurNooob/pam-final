package com.example.firebasetst;

import android.content.Intent; // Import Intent
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // If user is logged in, skip the landing page and go to MainActivity
            navigateToMainActivity();
            return; // Prevent further execution of the code in this method
        }

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        Button startButton = findViewById(R.id.startButton);
        LinearLayout indicatorLayout = findViewById(R.id.indicatorLayout);

        LandingPageAdapter adapter = new LandingPageAdapter(this);
        viewPager.setAdapter(adapter);

        OnboardingIndicators onboardingIndicators = new OnboardingIndicators(this, indicatorLayout);
        onboardingIndicators.setupIndicators(adapter.getItemCount());

        // Handle indicator changes on page scroll
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Update indicators
                onboardingIndicators.setActiveIndicator(position);

                // Handle visibility of the Start button
                if (position == adapter.getItemCount() - 1) {
                    startButton.setVisibility(View.VISIBLE);
                } else {
                    startButton.setVisibility(View.GONE);
                }
            }
        });

        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to navigate to MainActivity
    private void navigateToMainActivity() {
        Intent intent = new Intent(LandingPageActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close the LandingPageActivity so the user can't go back
    }
}