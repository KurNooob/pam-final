package com.example.firebasetst;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class OnboardingIndicators {

    private final Context context;
    private final LinearLayout indicatorLayout;

    public OnboardingIndicators(@NonNull Context context, @NonNull LinearLayout indicatorLayout) {
        this.context = context;
        this.indicatorLayout = indicatorLayout;
    }

    /**
     * Initializes the indicators (dots) based on the total number of pages.
     *
     * @param count The number of indicators (pages).
     */
    public void setupIndicators(int count) {
        // Clear existing indicators to avoid duplicates
        indicatorLayout.removeAllViews();

        // Create indicators
        for (int i = 0; i < count; i++) {
            View dot = new View(context);
            dot.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
            dot.setBackground(getInactiveIndicatorDrawable());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dot.getLayoutParams();
            params.setMargins(8, 0, 8, 0); // Add spacing between dots
            dot.setLayoutParams(params);

            indicatorLayout.addView(dot);
        }
    }

    /**
     * Sets the active indicator for the current page.
     *
     * @param position The position of the active page.
     */
    public void setActiveIndicator(int position) {
        int childCount = indicatorLayout.getChildCount();

        // Iterate over all indicators and update their drawable
        for (int i = 0; i < childCount; i++) {
            View dot = indicatorLayout.getChildAt(i);
            if (i == position) {
                dot.setLayoutParams(new LinearLayout.LayoutParams(60, 20)); // Proportional oval size
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dot.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                dot.setLayoutParams(params);
                dot.setBackground(getActiveIndicatorDrawable());
            } else {
                dot.setLayoutParams(new LinearLayout.LayoutParams(20, 20)); // Default size for inactive
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dot.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                dot.setLayoutParams(params);
                dot.setBackground(getInactiveIndicatorDrawable());
            }
        }
    }

    /**
     * Creates the drawable for the inactive indicator.
     *
     * @return A drawable for inactive indicators.
     */
    private GradientDrawable getInactiveIndicatorDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        drawable.setSize(20, 20);
        drawable.setDither(true); // Ensures smooth rendering
        return drawable;
    }

    /**
     * Creates the drawable for the active indicator.
     *
     * @return A drawable for active indicators.
     */
    private GradientDrawable getActiveIndicatorDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(ContextCompat.getColor(context, android.R.color.white));
        drawable.setSize(38, 25); // Smooth oval size for active indicator
        drawable.setDither(true); // Ensures smooth rendering
        return drawable;
    }
}
