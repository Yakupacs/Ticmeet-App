package com.mtaner.android_ticmeet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtaner.android_ticmeet.databinding.RecyclerRowBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.imageHolder> {

    private ArrayList<Event> eventArrayList;

    public imageAdapter(ArrayList<Event> eventArrayList) {
        this.eventArrayList = eventArrayList;
    }

    @NonNull
    @Override
    public imageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new imageHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull imageHolder holder, int position) {
        holder.recyclerRowBinding.txtKatagori.setText(eventArrayList.get(position).category);
        Picasso.get().load(eventArrayList.get(position).eventImage).into(holder.recyclerRowBinding.imageEvent);
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    class imageHolder extends RecyclerView.ViewHolder{
        RecyclerRowBinding recyclerRowBinding;

        public imageHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding=recyclerRowBinding;
        }
    }

}



