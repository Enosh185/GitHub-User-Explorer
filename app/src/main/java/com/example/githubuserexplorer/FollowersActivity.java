package com.example.githubuserexplorer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private EditText searchBar;
    private ShimmerFrameLayout shimmer;
    private GitHubApi api;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String login; // ✅ Needed globally

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerView);
        shimmer = findViewById(R.id.shimmer_view_container);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        searchBar = findViewById(R.id.search_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);

        api = RetrofitClient.getRetrofitInstance().create(GitHubApi.class);

        login = getIntent().getStringExtra("login");
        if (login == null || login.isEmpty()) {
            Toast.makeText(this, "No user specified", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔄 Pull-to-refresh logic
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (adapter != null) {
                adapter.clearUsers();
            }
            loadFollowers(login);
        });

        // 🔍 Live typing filter
        searchBar.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) adapter.filter(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        });

        // 🔎 Trigger search on "Enter" key press
        searchBar.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                if (adapter != null) adapter.filter(v.getText().toString().trim());
                return true;
            }
            return false;
        });

        // 🚀 Initial load
        loadFollowers(login);
    }

    private void loadFollowers(String login) {
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmer();
        swipeRefreshLayout.setRefreshing(false);

        api.getFollowers(login).enqueue(new Callback<List<GitHubUser>>() {
            @Override
            public void onResponse(Call<List<GitHubUser>> call, Response<List<GitHubUser>> response) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    adapter = new UserAdapter(response.body(), FollowersActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(FollowersActivity.this, "No followers found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GitHubUser>> call, Throwable t) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(FollowersActivity.this, "Failed to load followers", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
