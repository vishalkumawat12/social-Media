package com.example.socialmedia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;
import com.example.socialmedia.databinding.DashboardRvSampleBinding;
import com.example.socialmedia.model.DashBoard;

import java.util.ArrayList;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.ViewHolder> {
    ArrayList<DashBoard> list;
    Context context;

    public DashBoardAdapter(ArrayList<DashBoard> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_sample, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.profileImage.setImageResource(list.get(position).getProfile());
        holder.binding.postImage.setImageResource(list.get(position).getProfile());
        holder.binding.UserName.setText(list.get(position).getName());
        holder.binding.about.setText(list.get(position).getAbout());
        holder.binding.like.setText(list.get(position).getLike());
        holder.binding.go.setText(list.get(position).getComment());
        holder.binding.share.setText(list.get(position).getShare());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        DashboardRvSampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DashboardRvSampleBinding.bind(itemView);
        }
    }
}
