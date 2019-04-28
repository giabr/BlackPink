package com.example.searchfilter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder> {

    ArrayList<User> list;

    Context context;

    public AdapterClass(ArrayList<User> list, Context context){

        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.people_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(list.get(i).getName());
        viewHolder.status.setText(list.get(i).getStatus());
        viewHolder.follower.setText(String.valueOf(list.get(i).getFollower()));
        Glide.with(context).load(list.get(i).getImg()).into(viewHolder.image);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = list.get(i).getName();
                Intent intent = new Intent(v.getContext(), Profile.class);
                intent.putExtra("profile",user_id);
                v.getContext().startActivity(intent);
                Log.i("Clicked", "" + user_id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,status,follower;
        ImageView image;

        public ViewHolder(View view){
            super(view);
            follower = view.findViewById(R.id.follower);
            name = view.findViewById(R.id.name);
            status = view.findViewById(R.id.status);
            image = view.findViewById(R.id.img);
        }

    }

}
