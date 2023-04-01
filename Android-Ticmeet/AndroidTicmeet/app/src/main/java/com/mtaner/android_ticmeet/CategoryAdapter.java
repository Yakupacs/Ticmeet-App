package com.mtaner.android_ticmeet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    // variables for array list and context.
    private ArrayList<Category> categoriesArrayList;
    private Context context;

    // creating a constructor
    public CategoryAdapter(ArrayList<Category> categoriesArrayList, Context context) {
        this.categoriesArrayList = categoriesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new CategoryAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder,int position) {
        // setting data to our views on below line.
        Category modal = categoriesArrayList.get(position);
        holder.textCategory2.setText(modal.getEventCategory());
        EventAdapter adapter = new EventAdapter(modal.getEventArrayList(), context);
        // below line is for setting a layout manager for our recycler view.
        // here we are creating horizontal list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.idrecyclerevent.setLayoutManager(linearLayoutManager);
        holder.idrecyclerevent.setAdapter(adapter);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent intent =new Intent(holder.itemView.getContext(),ActivityEtkinliklerim.class);
                    intent.putExtra("Event",categoriesArrayList.get(pos));
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of array list on below line.
        return categoriesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating new variables for our views.
        private RecyclerView idrecyclerevent;
        private TextView textCategory2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views.
            textCategory2 = itemView.findViewById(R.id.textCategory2);
            idrecyclerevent = itemView.findViewById(R.id.idrecyclerevent);
        }
    }
}
