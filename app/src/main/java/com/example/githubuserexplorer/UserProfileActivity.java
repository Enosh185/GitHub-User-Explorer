package com.example.githubuserexplorer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView avatar;
    private TextView username, name, bio, followers, following;
    private LinearLayout profileContainer, followSection;
    private ShimmerFrameLayout shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        shimmer = findViewById(R.id.shimmer_view_container);
        avatar = findViewById(R.id.avatar);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        bio = findViewById(R.id.bio);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        profileContainer = findViewById(R.id.profile_container);
        followSection = findViewById(R.id.follow_section);

        // 🔒 Hide UI initially
        avatar.setVisibility(View.INVISIBLE);
        profileContainer.setVisibility(View.GONE);
        followSection.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmer();

        String login = getIntent().getStringExtra("username");
        if (login == null || login.isEmpty()) {
            Toast.makeText(this, "Invalid user", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        GitHubApi api = RetrofitClient.getRetrofitInstance().create(GitHubApi.class);
        api.getUser(login).enqueue(new Callback<GitHubUser>() {
            @Override
            public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    GitHubUser user = response.body();
                    profileContainer.setVisibility(View.VISIBLE);
                    followSection.setVisibility(View.VISIBLE);
                    avatar.setVisibility(View.VISIBLE);

                    Glide.with(UserProfileActivity.this)
                            .load(user.getAvatarUrl())
                            .circleCrop()
                            .into(avatar);

                    username.setText("@" + user.getLogin());
                    name.setText(user.getName() != null ? user.getName() : "—");
                    bio.setText(user.getBio() != null ? user.getBio() : "");
                    followers.setText(user.getFollowers() + " Followers");
                    following.setText(user.getFollowing() + " Following");

                } else {
                    Toast.makeText(UserProfileActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GitHubUser> call, Throwable t) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                Toast.makeText(UserProfileActivity.this, "Failed to load user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
