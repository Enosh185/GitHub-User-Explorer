package com.example.githubuserexplorer;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GitHubUser.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao(); // called in your ViewModel
}
