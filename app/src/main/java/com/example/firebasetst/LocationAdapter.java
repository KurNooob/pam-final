package com.example.firebasetst;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    public static final int VIEW_TYPE_HORIZONTAL = 0;
    public static final int VIEW_TYPE_VERTICAL = 1;

    private Context context;
    private List<Location> locations;
    private int viewType;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Location location);
    }

    public LocationAdapter(Context context, List<Location> locations, int viewType, OnItemClickListener listener) {
        this.context = context;
        this.locations = locations;
        this.viewType = viewType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = (viewType == VIEW_TYPE_HORIZONTAL) ? R.layout.item_card_horizontal : R.layout.item_card;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.titleText.setText(location.getTitle());
        holder.locationText.setText(location.getLocation());
        holder.ratingText.setText(location.getRating());
        holder.imageView.setImageResource(location.getImageResId());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(location));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, locationText, ratingText;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            locationText = itemView.findViewById(R.id.locationText);
            ratingText = itemView.findViewById(R.id.ratingText);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
