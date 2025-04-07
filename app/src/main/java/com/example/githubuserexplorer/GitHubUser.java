package com.example.githubuserexplorer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class GitHubUser {

    @PrimaryKey
    public int id;

    public String login;
    public String name;
    public String bio;

    @SerializedName("avatar_url")
    public String avatarUrl;

    public int followers;
    public int following;

    // Getters
    public String getLogin() { return login; }
    public String getName() { return name; }
    public String getBio() { return bio; }
    public String getAvatarUrl() { return avatarUrl; }
    public int getFollowers() { return followers; }
    public int getFollowing() { return following; }
}
