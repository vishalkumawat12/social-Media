package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.socialmedia.adapter.CommetAdapter;
import com.example.socialmedia.databinding.ActivityCommentBinding;
import com.example.socialmedia.model.CommentModel;
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

public class CommentActivity extends AppCompatActivity {
    ActivityCommentBinding binding;
    String postId;
    String postBy;
    ArrayList<CommentModel> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar2);
        CommentActivity.this.setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        postBy = intent.getStringExtra("postedBy");
        FirebaseDatabase.getInstance().getReference().child("posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get().load(post.getPostImage()).placeholder(R.drawable.u).into(binding.postImage);
                binding.discripion.setText(post.getPostDescription());
                binding.like.setText(post.getPostLike() + "");
                binding.comment.setText(post.getCommentCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(postBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                Picasso.get().load(userModel.getProfile()).placeholder(R.drawable.u).into(binding.profileImage);
                binding.name.setText(userModel.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentModel commentModel=new CommentModel();
                commentModel.setCommentBody(binding.editTextTextPersonName.getText().toString());
                commentModel.setCommentAt(new Date().getTime());
                commentModel.setCommentBy(FirebaseAuth.getInstance().getUid());
                FirebaseDatabase.getInstance().getReference().child("posts")
                        .child(postId).child("comments")
                        .push().setValue(commentModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                           FirebaseDatabase.getInstance().getReference()
                           .child("posts")
                           .child(postId).child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   int commentCount=0;
                                   if (snapshot.exists())
                                   {
                                       commentCount=snapshot.getValue(Integer.class);
                                   }
                                   FirebaseDatabase.getInstance().getReference()
                                           .child("posts")
                                           .child(postId).child("commentCount")
                                           .setValue(++commentCount).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void unused) {
                                           binding.editTextTextPersonName.setText("");
                                           Toast.makeText(CommentActivity.this, "Commented", Toast.LENGTH_SHORT).show();

                                           NotificationModel notificationModel=new NotificationModel();
                                           notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                           notificationModel.setNotificationAt(new Date().getTime());
                                           notificationModel.setPostedBy(postBy);
                                           notificationModel.setType("comment");
                                           FirebaseDatabase.getInstance().getReference()
                                                   .child("notification")
                                                   .child(postBy).push()
                                                   .setValue(notificationModel);
                                       }
                                   });
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });
                            }
                        });
            }
        });

        CommetAdapter aadpter=new CommetAdapter(this,list);
        binding.recyclerViewMAin.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMAin.setAdapter(aadpter);

        FirebaseDatabase.getInstance().getReference().child("posts").child(postId).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    CommentModel commentModel=dataSnapshot.getValue(CommentModel.class);
                    list.add(commentModel);


                }
                aadpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}