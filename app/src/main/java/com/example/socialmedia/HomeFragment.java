package com.example.socialmedia;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.socialmedia.adapter.PostAdapter;
import com.example.socialmedia.adapter.StoryAdapter;
import com.example.socialmedia.databinding.FragmentHomeBinding;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.StoryModel;
import com.example.socialmedia.model.UsersStory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ArrayList<StoryModel> storyList = new ArrayList<>();
    ArrayList<Post> postArrayList = new ArrayList<>();
    PostAdapter postAdapter;
    ActivityResultLauncher<String> galleyLounch;
    ProgressDialog dialog;
    ShimmerRecyclerView shimmerRecycler;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog=new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
         shimmerRecycler= (ShimmerRecyclerView)binding.bashBoardRV;
        binding.bashBoardRV.showShimmerAdapter();

        StoryAdapter adapter = new StoryAdapter(storyList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.StoryRV.setLayoutManager(layoutManager);
        binding.StoryRV.setNestedScrollingEnabled(false);
        binding.StoryRV.setAdapter(adapter);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Story Uploading : ");
        dialog.setMessage("Please wait ...");
        dialog.setCancelable(false);


        FirebaseDatabase.getInstance().getReference()
                .child("stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            storyList.clear();
                            for (DataSnapshot storySnapshot:snapshot.getChildren())
                            {
                                StoryModel storyModel=new StoryModel();
                                storyModel.setStoryBy(storySnapshot.getKey());
                                storyModel.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));
                                ArrayList<UsersStory> stories=new ArrayList<>();
                                for (DataSnapshot snapshot1:storySnapshot.child("userStories").getChildren())
                                {

                                        UsersStory usersStory=snapshot1.getValue(UsersStory.class);
                                        stories.add(usersStory);


                                }
                                storyModel.setStories(stories);
                                storyList.add(storyModel);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        postAdapter = new PostAdapter(postArrayList, getContext());
        binding.bashBoardRV.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.bashBoardRV.setAdapter(postAdapter);
        binding.bashBoardRV.setNestedScrollingEnabled(false);

        FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    post.setPostId(dataSnapshot.getKey());
                    postArrayList.add(post);
                }
                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
            galleyLounch=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    binding.addStoryImg.setImageURI(result);
                    dialog.show();
                    final StorageReference reference= FirebaseStorage.getInstance().getReference().child("stories").child(FirebaseAuth.getInstance().getUid())

                            .child(new Date().getTime()+"");
                   reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                  StoryModel storyModel=new StoryModel();
                                  storyModel.setStoryAt(new Date().getTime());
                                   FirebaseDatabase.getInstance().getReference().child("stories")
                                  .child(FirebaseAuth.getInstance().getUid())
                                  .child("postedBy")
                                           .setValue(storyModel.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void unused) {
                                           UsersStory story=new UsersStory(uri.toString(),storyModel.getStoryAt());
                                           FirebaseDatabase.getInstance().getReference().child("stories")
                                                   .child(FirebaseAuth.getInstance().getUid())
                                                   .child("userStories")
                                                   .push()
                                                   .setValue(story).addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void unused) {
                                                   dialog.dismiss();
                                               }
                                           });
                                       }
                                   });

                               }
                           });

                       }
                   });

                }
            });
        binding.addStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleyLounch.launch("image/*");

            }
        });
        return binding.getRoot();
    }
}