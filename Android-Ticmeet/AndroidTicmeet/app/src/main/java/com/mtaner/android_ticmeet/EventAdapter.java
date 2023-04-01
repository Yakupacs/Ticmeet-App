package com.mtaner.android_ticmeet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    // variables for array list and context.
    private ArrayList<Event> eventArrayList;
    private Context context;

    // creating a constructor.
    public EventAdapter(ArrayList<Event> eventArrayList, Context context) {
        this.eventArrayList = eventArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new EventAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        // on below line we are setting data to our ui components.
        Event event = eventArrayList.get(position);
        holder.textCategory.setText(event.getCategory());

        Picasso.get().load(event.getEventImage()).into(holder.eventimage);
    }

    @Override
    public int getItemCount() {
        // returning the size of array list
        return eventArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text view.
        private TextView textCategory;
        private ImageView eventimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text view
            textCategory=itemView.findViewById(R.id.textCategory);
            eventimage = itemView.findViewById(R.id.eventimage);

        }
    }
}



