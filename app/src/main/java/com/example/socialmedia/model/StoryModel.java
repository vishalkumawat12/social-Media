package com.example.socialmedia.model;

import java.util.ArrayList;

public class StoryModel {

    private String storyBy;
    private long storyAt;
    ArrayList<UsersStory> stories=new ArrayList<>();

    public StoryModel() {
    }

    public String getStoryBy() {
        return storyBy;
    }

    public void setStoryBy(String storyBy) {
        this.storyBy = storyBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<UsersStory> getStories() {
        return stories;
    }

    public void setStories(ArrayList<UsersStory> stories) {
        this.stories = stories;
    }
}
