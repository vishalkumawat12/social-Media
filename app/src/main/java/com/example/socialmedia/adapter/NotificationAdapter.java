package com.example.socialmedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.CommentActivity;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.NotiSampleBinding;
import com.example.socialmedia.model.NotificationModel;
import com.example.socialmedia.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

ArrayList<NotificationModel> notificationModels=new ArrayList<>();
Context context;

    public NotificationAdapter(ArrayList<NotificationModel> notificationModels, Context context) {
        this.notificationModels = notificationModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.noti_sample,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notificationModel=notificationModels.get(position);
        String type=notificationModel.getType();
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notificationModel.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel=snapshot.getValue(UserModel.class);

                        Picasso.get().load(userModel.getProfile()).placeholder(R.drawable.u).into(holder.binding.profileImage);
                    if (type.equals("like"))
                    {
                        holder.binding.textView12.setText(Html.fromHtml("<b>" +userModel.getName()+ "</b>"+" liked your post"));
                    }else if (type.equals("comment"))
                    {
                        holder.binding.textView12.setText(Html.fromHtml("<b>" +userModel.getName()+ "</b>"+" commented on your post"));

                    }else {
                        holder.binding.textView12.setText(Html.fromHtml("<b>" +userModel.getName()+ "</b>"+" start follow you"));

                    }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.binding.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("notification").child(notificationModel.getPostedBy())
                            .child(notificationModel.getNotificationId()).child("checkOpen").setValue(true);
                    holder.binding.main.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("postId", notificationModel.getPostId());
                intent.putExtra("postedBy", notificationModel.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);}
            }
        });
        Boolean check=notificationModel.isCheckOpen();
        if (check==true){
            holder.binding.main.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }else {}

    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder {
        NotiSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=NotiSampleBinding.bind(itemView);
        }
    }
}
