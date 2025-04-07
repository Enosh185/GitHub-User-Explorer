package com.example.githubuserexplorer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

public class MainActivity extends AppCompatActivity {

    private EditText searchInput;
    private Button searchButton;
    private ImageView avatar;
    private TextView username, name, bio, followers, following, notFoundText;
    private LinearLayout profileContainer, followSection;
    private ShimmerFrameLayout shimmerLayout;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🌙 Theme setup
        Switch darkModeSwitch = findViewById(R.id.dark_mode_switch);
        SharedPreferences prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        darkModeSwitch.setChecked(isDark);
        darkModeSwitch.jumpDrawablesToCurrentState();
        darkModeSwitch.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean("dark_mode", checked).apply();
            AppCompatDelegate.setDefaultNightMode(checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        // 🧭 Toolbar setup
        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("GitHub Explorer");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        // 🔗 UI elements
        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);
        avatar = findViewById(R.id.avatar);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        bio = findViewById(R.id.bio);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        profileContainer = findViewById(R.id.profile_container);
        followSection = findViewById(R.id.follow_section);
        notFoundText = findViewById(R.id.not_found_text);
        shimmerLayout = findViewById(R.id.shimmer_view_container);


        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        clearUserData();

        // 🔍 Search button
        searchButton.setOnClickListener(v -> {
            String user = searchInput.getText().toString().trim();
            if (!user.isEmpty()) {
                clearUserData();
                shimmerLayout.setVisibility(View.VISIBLE);
                shimmerLayout.startShimmer();
                viewModel.fetchUser(user);
            }
        });

        // 📡 Observer for user result
        viewModel.getUser().observe(this, user -> {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);

            if (user != null && user.getLogin() != null) {
                notFoundText.setVisibility(View.GONE); // 👈 HIDE not found

                profileContainer.setVisibility(View.VISIBLE);
                followSection.setVisibility(View.VISIBLE);
                avatar.setVisibility(View.VISIBLE);

                Glide.with(this).load(user.getAvatarUrl()).circleCrop().into(avatar);
                username.setText("@" + user.getLogin());
                name.setText(user.getName() != null ? user.getName() : "—");
                bio.setText(user.getBio() != null ? user.getBio() : "");
                followers.setText(user.getFollowers() + " Followers");
                following.setText(user.getFollowing() + " Following");

                followers.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, FollowersActivity.class);
                    intent.putExtra("login", user.getLogin());
                    startActivity(intent);
                });

                following.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, FollowingActivity.class);
                    intent.putExtra("login", user.getLogin());
                    startActivity(intent);
                });

            } else{
                clearUserData();
                notFoundText.setVisibility(View.VISIBLE); // 👈 SHOW not found
                Toast.makeText(MainActivity.this, "No GitHub user found with that name", Toast.LENGTH_SHORT).show();
            }
        });

        // ⏳ Show shimmer while loading
        viewModel.getLoading().observe(this, isLoading -> {
            if (isLoading) {
                shimmerLayout.setVisibility(View.VISIBLE);
                shimmerLayout.startShimmer();
                profileContainer.setVisibility(View.GONE);
                followSection.setVisibility(View.GONE);
                notFoundText.setVisibility(View.GONE); // 👈 HIDE not found while loading
            }
        });
    }

    private void clearUserData() {
        profileContainer.setVisibility(View.GONE);
        followSection.setVisibility(View.GONE);
        shimmerLayout.setVisibility(View.GONE);
        avatar.setVisibility(View.INVISIBLE);
        username.setText("");
        name.setText("");
        bio.setText("");
        followers.setText("");
        following.setText("");
        notFoundText.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
