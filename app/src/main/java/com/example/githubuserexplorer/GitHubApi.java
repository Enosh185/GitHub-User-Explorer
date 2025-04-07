package com.example.githubuserexplorer;
import retrofit2.http.Query;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubApi {

    // Get single user profile
    @GET("users/{username}")
    Call<GitHubUser> getUser(@Path("username") String username);

    // Get followers list
    @GET("users/{user}/followers")
    Call<List<GitHubUser>> getFollowers(@Path("user") String username);

    @GET("users/{user}/following")
    Call<List<GitHubUser>> getFollowing(@Path("user") String username);


}
