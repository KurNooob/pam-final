package com.example.firebasetst;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private FirebaseFirestore db;
    private String currentUserId;
    private List<Location> favoriteLocations;
    private List<Location> allLocations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        favoriteLocations = new ArrayList<>();

        initializeAllLocations();
        fetchFavorites();

        return view;
    }

    private void initializeAllLocations() {
        allLocations = new ArrayList<>();
        allLocations.add(new Location("1", "Malioboro", "Gedong Tengen, Yogyakarta City", "4.7", R.drawable.image_malio, "A bustling street known for its shops and cultural charm."));
        allLocations.add(new Location("2", "Benteng Vredeburg", "Ngupasan, Yogyakarta City", "4.6", R.drawable.image_bente, "A historical fortress turned museum in the heart of Yogyakarta."));
        allLocations.add(new Location("3", "Pantai Parangtritis", "Kretek, Bantul", "4.6", R.drawable.image_panta, "A beautiful beach with stunning sunsets and cultural significance."));
        allLocations.add(new Location("4", "Museum Sonobudoyo", "Ngupasan, Yogyakarta City", "4.7", R.drawable.image_museu, "A museum showcasing Javanese culture and history."));
        allLocations.add(new Location("5", "Taman Pintar", "Ngupasan, Yogyakarta City", "4.5", R.drawable.image_taman, "An educational park perfect for family outings."));
        allLocations.add(new Location("6", "Taman Sari", "Patehan, Yogyakarta City", "4.6", R.drawable.image_tamsari, "A historic royal garden with unique architecture."));
        allLocations.add(new Location("7", "Kraton Yogyakarta", "Panembahan, Yogyakarta City", "4.7", R.drawable.image_krato, "The Sultan's palace and a symbol of Yogyakarta's heritage."));
    }

    private void fetchFavorites() {
        db.collection("favorites")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<String> locationIds = new ArrayList<>();

                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String locationId = document.getString("locationId");
                        if (locationId != null) {
                            locationIds.add(locationId);
                        }
                    }

                    filterFavoriteLocations(locationIds);
                })
                .addOnFailureListener(e -> {
                    // Handle failure here
                });
    }

    private void filterFavoriteLocations(List<String> locationIds) {
        for (Location location : allLocations) {
            if (locationIds.contains(location.getId())) {
                favoriteLocations.add(location);
            }
        }

        adapter = new LocationAdapter(getContext(), favoriteLocations, LocationAdapter.VIEW_TYPE_VERTICAL, location -> {
            openLocationDetails(location);
        });
        recyclerView.setAdapter(adapter);
    }

    private void openLocationDetails(Location location) {
        Intent intent = new Intent(getActivity(), LocationDetailsActivity.class);
        intent.putExtra("id", location.getId());
        intent.putExtra("title", location.getTitle());
        intent.putExtra("location", location.getLocation());
        intent.putExtra("rating", location.getRating());
        intent.putExtra("overview", location.getOverview());
        startActivity(intent);
    }
}
