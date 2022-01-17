package com.example.socialmedia.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;
import com.example.socialmedia.databinding.CommentSampleBinding;
import com.example.socialmedia.model.CommentModel;
import com.example.socialmedia.model.UserModel;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommetAdapter extends RecyclerView.Adapter<CommetAdapter.ViewHolder> {
Context context;
ArrayList<CommentModel> list;

    public CommetAdapter(Context context, ArrayList<CommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.comment_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentModel commentModel=list.get(position);
       // holder.binding.comment.setText(commentModel.getCommentBody());
        String text = TimeAgo.using(commentModel.getCommentAt());
        holder.binding.time.setText(text);
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(commentModel.getCommentBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel=snapshot.getValue(UserModel.class);
                Picasso.get().load(userModel.getProfile()).placeholder(R.drawable.u).into(holder.binding.profileImage);
                holder.binding.comment.setText(Html.fromHtml("<b>"+userModel.getName()+"</b>"+"  "+commentModel.getCommentBody()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CommentSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CommentSampleBinding.bind(itemView);
        }
    }
}
