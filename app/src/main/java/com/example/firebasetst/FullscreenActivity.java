package com.example.firebasetst;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class FullscreenActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ImageView ivLeftArrow, ivRightArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        // Find views
        viewPager = findViewById(R.id.viewPager);
        ivLeftArrow = findViewById(R.id.ivLeftArrow);
        ivRightArrow = findViewById(R.id.ivRightArrow);

        // Get image data (resource IDs)
        int[] imageList = getIntent().getIntArrayExtra("imageList");

        // Set up adapter for ViewPager2
        ImagePagerAdapter adapter = new ImagePagerAdapter(imageList, this);
        viewPager.setAdapter(adapter);

        // Handle arrow visibility
        updateArrowVisibility(viewPager.getCurrentItem(), imageList.length);

        // Page change listener
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateArrowVisibility(position, imageList.length);
            }
        });

        // Arrow click listeners
        ivLeftArrow.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true));
        ivRightArrow.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true));
    }

    // Update the visibility of the arrows
    private void updateArrowVisibility(int position, int total) {
        ivLeftArrow.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
        ivRightArrow.setVisibility(position < total - 1 ? View.VISIBLE : View.GONE);
    }
}

