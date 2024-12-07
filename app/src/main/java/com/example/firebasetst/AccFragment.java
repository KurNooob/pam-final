package com.example.firebasetst;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText usernameText, genderText, emailText;
    private TextView editButton;
    private Button logoutButton, changePasswordButton;
    private boolean isEditing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acc, container, false);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Reference to views from XML
        usernameText = view.findViewById(R.id.usernameText);
        genderText = view.findViewById(R.id.genderText);
        emailText = view.findViewById(R.id.emailText);
        editButton = view.findViewById(R.id.editButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        changePasswordButton = view.findViewById(R.id.changPasswordButton);

        // Initially set fields to non-editable
        setEditable(false);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loadUserData(user.getUid());
        }

        // Edit button logic
        editButton.setOnClickListener(v -> {
            if (isEditing) {
                // Save data if in editing mode
                saveUserData();
                // After saving, set fields to non-editable
                setEditable(false);
                // Change button text back to "Ubah"
                editButton.setText("Ubah");
                // Make logout and change password buttons visible
                logoutButton.setVisibility(View.VISIBLE);
                changePasswordButton.setVisibility(View.VISIBLE);
                // Update editing state
                isEditing = false;
            } else {
                // Enable editing mode
                setEditable(true);
                // Change button text to "Simpan"
                editButton.setText("Simpan");
                // Make logout and change password buttons invisible
                logoutButton.setVisibility(View.INVISIBLE);
                changePasswordButton.setVisibility(View.INVISIBLE);
                // Update editing state
                isEditing = true;
            }
        });

        // Logout button logic
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Change Password button logic (no function for now)
        changePasswordButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Ganti Kata Sandi belum tersedia", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    // Method to load user data from Firestore
    private void loadUserData(String userId) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String gender = documentSnapshot.getString("gender");
                String email = documentSnapshot.getString("email");

                usernameText.setText(name);
                genderText.setText(gender);
                emailText.setText(email);
            } else {
                Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    // Save updated user data
    private void saveUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        String newName = usernameText.getText().toString().trim();
        String newGender = genderText.getText().toString().trim();
        String newEmail = emailText.getText().toString().trim();

        // Validate fields
        if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newGender) || TextUtils.isEmpty(newEmail)) {
            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update email if changed
        if (!newEmail.equals(user.getEmail())) {
            user.updateEmail(newEmail)
                    .addOnSuccessListener(aVoid -> updateFirestoreData(userId, newName, newGender, newEmail))
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Email update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        } else {
            updateFirestoreData(userId, newName, newGender, newEmail);
        }
    }

    // Update Firestore data
    private void updateFirestoreData(String userId, String newName, String newGender, String newEmail) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", newName);
        userData.put("gender", newGender);
        userData.put("email", newEmail);

        db.collection("users").document(userId)
                .update(userData)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(getContext(), "User data updated successfully", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    // Toggle field editability
    private void setEditable(boolean editable) {
        usernameText.setEnabled(editable);
        genderText.setEnabled(editable);
        emailText.setEnabled(editable);
    }
}
