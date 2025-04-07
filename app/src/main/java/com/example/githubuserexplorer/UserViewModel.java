package com.example.githubuserexplorer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {
    private final MutableLiveData<GitHubUser> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final AppDatabase db;

    private String lastLogin = "octocat";

    public UserViewModel(@NonNull Application application) {
        super(application);
        db = Room.databaseBuilder(application, AppDatabase.class, "github_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public LiveData<GitHubUser> getUser() {
        return user;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void fetchUser(String login) {
        lastLogin = login;
        loading.setValue(true);

        // Load from cache first
        GitHubUser cached = db.userDao().getUser(login);
        if (cached != null) {
            user.setValue(cached);
        }

        // Fetch from API
        GitHubApi api = RetrofitClient.getRetrofitInstance().create(GitHubApi.class);
        api.getUser(login).enqueue(new Callback<GitHubUser>() {
            @Override
            public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {
                loading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    GitHubUser fresh = response.body();
                    user.setValue(fresh);
                    db.userDao().insert(fresh); // ✅ cache it
                } else {
                    user.setValue(null); // ✅ Notify UI of not-found
                }
            }

            @Override
            public void onFailure(Call<GitHubUser> call, Throwable t) {
                loading.setValue(false);
                user.setValue(null); // ✅ Notify UI of failure
            }
        });
    }

    public void refreshUser() {
        fetchUser(lastLogin);
    }
}
