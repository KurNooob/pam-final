package com.example.firebasetst;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        if (review != null) {
            // Set user name
            holder.userNameTextView.setText(review.getUserName());

            // Set review description
            holder.descriptionTextView.setText(review.getDescription());

            // Set rating value
            holder.ratingBar.setRating((float) review.getRating());

            // Set user profile image (placeholder for now)
            holder.userImageView.setImageResource(R.drawable.acccrp);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        final TextView userNameTextView;
        final TextView descriptionTextView;
        final RatingBar ratingBar;
        final ImageView userImageView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            userImageView = itemView.findViewById(R.id.userImageView);
        }
    }
}
