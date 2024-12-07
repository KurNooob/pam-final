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

import java.util.ArrayList;
import java.util.List;

public class LocFragment extends Fragment {

    private RecyclerView recyclerViewPopular;
    private RecyclerView recyclerViewRecommended;
    private LocationAdapter popularAdapter;
    private LocationAdapter recommendedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loc, container, false);

        // Initialize RecyclerViews
        recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular);
        recyclerViewRecommended = view.findViewById(R.id.recyclerViewRecommended);

        // Set up "Populer di Sekitarmu" (Horizontal)
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Location> popularLocations = getPopularLocations();
        popularAdapter = new LocationAdapter(getContext(), popularLocations, LocationAdapter.VIEW_TYPE_HORIZONTAL, location -> {
            openLocationDetails(location);
        });
        recyclerViewPopular.setAdapter(popularAdapter);

        // Set up "Rekomendasi" (Vertical)
        recyclerViewRecommended.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Location> recommendedLocations = getRecommendedLocations();
        recommendedAdapter = new LocationAdapter(getContext(), recommendedLocations, LocationAdapter.VIEW_TYPE_VERTICAL, location -> {
            openLocationDetails(location);
        });
        recyclerViewRecommended.setAdapter(recommendedAdapter);

        return view;
    }

    // Function to get the list of popular locations
    private List<Location> getPopularLocations() {
        List<Location> popularLocations = new ArrayList<>();
        popularLocations.add(new Location(
                "1", "Malioboro", "Gedong Tengen, Yogyakarta City", "4.7",
                R.drawable.image_malio, "A bustling street known for its shops and cultural charm."
        ));
        popularLocations.add(new Location(
                "2", "Benteng Vredeburg", "Ngupasan, Yogyakarta City", "4.6",
                R.drawable.image_bente, "A historical fortress turned museum in the heart of Yogyakarta."
        ));
        return popularLocations;
    }

    // Function to get the list of recommended locations
    private List<Location> getRecommendedLocations() {
        List<Location> recommendedLocations = new ArrayList<>();
        recommendedLocations.add(new Location(
                "3", "Pantai Parangtritis", "Kretek, Bantul", "4.6",
                R.drawable.image_panta, "Pantai Parangtritis adalah tempat wisata yang terletak di Kalurahan Parangtritis, Kapanéwon Kretek, Kabupaten Bantul, Daerah Istimewa Yogyakarta. Jaraknya kurang lebih 27 km dari pusat kota. Pantai ini menjadi salah satu destinasi wisata terkenal di Yogyakarta dan telah menjadi ikon pariwisata di Yogyakarta."
        ));
        recommendedLocations.add(new Location(
                "4", "Museum Sonobudoyo", "Ngupasan, Yogyakarta City", "4.7",
                R.drawable.image_museu, "A museum showcasing Javanese culture and history."
        ));
        recommendedLocations.add(new Location(
                "5", "Taman Pintar", "Ngupasan, Yogyakarta City", "4.5",
                R.drawable.image_taman, "An educational park perfect for family outings."
        ));
        recommendedLocations.add(new Location(
                "6", "Taman Sari", "Patehan, Yogyakarta City", "4.6",
                R.drawable.image_tamsari, "A historic royal garden with unique architecture."
        ));
        recommendedLocations.add(new Location(
                "7", "Kraton Yogyakarta", "Panembahan, Yogyakarta City", "4.7",
                R.drawable.image_krato, "The Sultan's palace and a symbol of Yogyakarta's heritage."
        ));
        return recommendedLocations;
    }

    // Function to open location details
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
