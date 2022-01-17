package com.example.socialmedia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;
import com.example.socialmedia.databinding.RvFriendsBinding;
import com.example.socialmedia.model.Follow;
import com.example.socialmedia.model.FriendModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{
ArrayList<Follow> modelArrayList=new ArrayList<>();
Context context;

    public FriendAdapter(ArrayList<Follow> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_friends,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(modelArrayList.get(position).getProfile()).placeholder(R.drawable.u).into(holder.binding.profileImage);
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RvFriendsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=RvFriendsBinding.bind(itemView);
        }
    }
}
