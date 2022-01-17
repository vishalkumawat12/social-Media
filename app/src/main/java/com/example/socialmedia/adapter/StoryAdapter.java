package com.example.socialmedia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;
import com.example.socialmedia.databinding.StoryRvDesignBinding;
import com.example.socialmedia.model.StoryModel;
import com.example.socialmedia.model.UserModel;
import com.example.socialmedia.model.UsersStory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.VIewHOlder> {
    ArrayList<StoryModel> storyModels;
    Context context;

    public StoryAdapter(ArrayList<StoryModel> storyModels, Context context) {
        this.storyModels = storyModels;
        this.context = context;

    }

    @NonNull
    @Override
    public VIewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_rv_design, parent, false);
        return new VIewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VIewHOlder holder, int position) {
        StoryModel storyModel = storyModels.get(position);
        if (storyModel.getStories().size() > 0) {
            UsersStory lastStory = storyModel.getStories().get(storyModel.getStories().size() - 1);
            Picasso.get().load(lastStory.getImage()).into(holder.binding.story);
            holder.binding.sotyAC.setPortionsCount(storyModel.getStories().size());

            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(storyModel.getStoryBy()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    Picasso.get().load(userModel.getProfile())
                            .placeholder(R.drawable.u)
                            .into(holder.binding.profileImage);

                    holder.binding.UserName.setText(userModel.getName());
                    holder.binding.story.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<MyStory> myStories = new ArrayList<>();

                            for (UsersStory story : storyModel.getStories()) {
                                myStories.add(new MyStory(
                                        story.getImage()


                                ));
                            }

                            new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                    .setStoriesList(myStories) // Required
                                    .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                    .setTitleText(userModel.getName()) // Default is Hidden
                                    .setSubtitleText("") // Default is Hidden
                                    .setTitleLogoUrl(userModel.getProfile()) // Default is Hidden
                                    .setStoryClickListeners(new StoryClickListeners() {
                                        @Override
                                        public void onDescriptionClickListener(int position) {
                                            //your action
                                        }

                                        @Override
                                        public void onTitleIconClickListener(int position) {
                                            //your action
                                        }
                                    }) // Optional Listeners
                                    .build() // Must be called before calling show method
                                    .show();


                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return storyModels.size();
    }

    public class VIewHOlder extends RecyclerView.ViewHolder {

        StoryRvDesignBinding binding;

        public VIewHOlder(@NonNull View itemView) {
            super(itemView);
            binding = StoryRvDesignBinding.bind(itemView);
        }
    }
}
