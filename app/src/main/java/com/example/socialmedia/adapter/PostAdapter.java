package com.example.socialmedia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.CommentActivity;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.DashboardRvSampleBinding;
import com.example.socialmedia.model.NotificationModel;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    ArrayList<Post> list = new ArrayList<>();
    Context context;

    public PostAdapter(ArrayList<Post> list, Context context) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = list.get(position);

        holder.binding.like.setText(post.getPostLike() + "");
        holder.binding.about.setText(post.getPostDescription());
        Picasso.get().load(post.getPostImage()).placeholder(R.drawable.u).into(holder.binding.postImage);
        holder.binding.go.setText(post.getCommentCount() + "");
        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                Picasso.get().load(userModel.getProfile()).placeholder(R.drawable.u).into(holder.binding.profileImage);
                holder.binding.UserName.setText(userModel.getName());
                holder.binding.about.setText(userModel.getProfession());
                if (!list.get(position).getPostDescription().equals("")) {


                    holder.binding.textView8.setText(post.getPostDescription());
                } else {
                    holder.binding.textView8.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("posts").child(post.getPostId())
                .child("likes").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_2, 0, 0, 0);

                } else {
                    holder.binding.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference().child("posts").child(post.getPostId()).child("likes").child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference().child("posts").child(post.getPostId()).child("postLike").setValue(post.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_2, 0, 0, 0);

                                            NotificationModel notificationModel = new NotificationModel();
                                            notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            notificationModel.setNotificationAt(new Date().getTime());
                                            notificationModel.setPostId(post.getPostId());
                                            notificationModel.setPostedBy(post.getPostedBy());
                                            notificationModel.setType("like");
                                            FirebaseDatabase.getInstance().getReference().child("notification").child(notificationModel.getPostedBy())
                                                    .push().setValue(notificationModel);
                                        }
                                    });
                                }
                            });
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.binding.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("postedBy", post.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        DashboardRvSampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DashboardRvSampleBinding.bind(itemView);
        }
    }
}
