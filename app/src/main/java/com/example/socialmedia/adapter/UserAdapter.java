package com.example.socialmedia.adapter;

import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;
import com.example.socialmedia.databinding.UserSampleBinding;
import com.example.socialmedia.model.Follow;
import com.example.socialmedia.model.NotificationModel;
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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    ArrayList<UserModel> modelArrayList;

    public UserAdapter(Context context, ArrayList<UserModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(context).inflate(R.layout.user_sample, parent, false);
        return new ViewHolder(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserModel user = modelArrayList.get(position);
        Picasso.get().load(user.getProfile()).placeholder(R.drawable.u).into(holder.binding.profileImage);
        holder.binding.name.setText(user.getName());
        holder.binding.profession.setText(user.getProfession());
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserID()).child("followers").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if (snapshot.exists())
             {

                 holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_inactive));
                 holder.binding.followBtn.setText("followed");
                 holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.grey));
                 holder.binding.followBtn.setEnabled(false);


             }else {

                 holder.binding.followBtn.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Follow follow=new Follow();
                         follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                         follow.setFollowedAt(new Date().getTime());

                         FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserID()).child("followers").child(FirebaseAuth.getInstance().getUid()).setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserID()).child("follerCount").setValue(user.getFollerCount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void unused) {
                                         holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_inactive));
                                         holder.binding.followBtn.setText("followed");
                                         holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.grey));
                                         holder.binding.followBtn.setEnabled(false);
                                         Toast.makeText(context, "You followed  "+user.getName(), Toast.LENGTH_SHORT).show();
                                         NotificationModel notification=new NotificationModel();
                                         notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                         notification.setNotificationAt(new Date().getTime());
                                         notification.setType("follow");
                                         FirebaseDatabase.getInstance().getReference().child("notification")
                                                 .child(user.getUserID()).push().setValue(notification);
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

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UserSampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UserSampleBinding.bind(itemView);
        }
    }
}
