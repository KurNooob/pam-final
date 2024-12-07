package com.example.firebasetst;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LandingPageAdapter extends FragmentStateAdapter {

    public LandingPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return LandingPageFragment.newInstance("Jelajah Dekat", "Temukan rekomendasi tempat \n" +
                        "wisata di sekitar Anda berdasarkan \n" +
                        "ulasan pengunjung.");
            case 1:
                return LandingPageFragment.newInstance("Rencanakan Rute", "Dapatkan panduan rute terbaik untuk \n" +
                        "menuju lokasi wisata pilihan Anda");
            case 2:
                return LandingPageFragment.newInstance("Bagikan Cerita", "Bagikan ulasan dan pengalaman Anda \n" +
                        "untuk membantu wisatawan lain \n" +
                        "menemukan destinasi terbaik.");
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Total number of pages
    }
}