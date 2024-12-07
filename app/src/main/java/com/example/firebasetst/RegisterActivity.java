package com.example.firebasetst;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText nameEditText, genderEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameEditText = findViewById(R.id.nameEditText);
        genderEditText = findViewById(R.id.genderEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String gender = genderEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (password.equals(confirmPassword) && !name.isEmpty() && !gender.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                registerUser(name, gender, email, password);
            } else {
                Toast.makeText(RegisterActivity.this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
            }
        });

        loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser(String name, String gender, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();

                            // Fetch or initialize the next user_id
                            db.collection("counters").document("userCounter")
                                    .get()
                                    .addOnSuccessListener(snapshot -> {
                                        if (!snapshot.exists()) {
                                            // Initialize userCounter document if it doesn't exist
                                            Map<String, Object> counterInit = new HashMap<>();
                                            counterInit.put("count", 1L);
                                            db.collection("counters").document("userCounter")
                                                    .set(counterInit)
                                                    .addOnSuccessListener(aVoid -> saveUserToFirestore(userId, 1L, name, gender, email))
                                                    .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error initializing user counter", Toast.LENGTH_SHORT).show());
                                        } else {
                                            // Increment the count and fetch its value
                                            db.collection("counters").document("userCounter")
                                                    .update("count", FieldValue.increment(1))
                                                    .addOnSuccessListener(aVoid -> {
                                                        db.collection("counters").document("userCounter")
                                                                .get()
                                                                .addOnSuccessListener(updatedSnapshot -> {
                                                                    long newUserId = updatedSnapshot.getLong("count");
                                                                    saveUserToFirestore(userId, newUserId, name, gender, email);
                                                                });
                                                    })
                                                    .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error incrementing user ID", Toast.LENGTH_SHORT).show());
                                        }
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error fetching user counter", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String userId, long userAutoId, String name, String gender, String email) {
        Map<String, Object> userMap = new HashMap<>();
        // Convert userAutoId to a String
        userMap.put("user_id", String.valueOf(userAutoId)); // Convert long to String
        userMap.put("name", name);
        userMap.put("gender", gender);
        userMap.put("email", email);

        db.collection("users").document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
