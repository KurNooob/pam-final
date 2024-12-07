package com.example.firebasetst;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DisplayDataActivity extends AppCompatActivity {

    private TextView fetchedDataTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        db = FirebaseFirestore.getInstance();
        fetchedDataTextView = findViewById(R.id.fetchedDataTextView);

        // Fetch data from Firestore
        fetchDataFromFirestore();


    }

    private void fetchDataFromFirestore() {
        db.collection("locations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        StringBuilder data = new StringBuilder();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Assuming the data contains fields: title, location, description, userEmail
                            String title = documentSnapshot.getString("title");
                            String location = documentSnapshot.getString("location");
                            String description = documentSnapshot.getString("description");
                            String userEmail = documentSnapshot.getString("userEmail");

                            // Build the string to display
                            data.append("Title: ").append(title)
                                    .append("\nLocation: ").append(location)
                                    .append("\nDescription: ").append(description)
                                    .append("\nUser: ").append(userEmail)
                                    .append("\n\n");
                        }
                        // Set the data to TextView
                        fetchedDataTextView.setText(data.toString());
                    } else {
                        Toast.makeText(DisplayDataActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DisplayDataActivity.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
