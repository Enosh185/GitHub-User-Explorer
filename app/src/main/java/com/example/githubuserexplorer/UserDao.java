package com.example.githubuserexplorer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT * FROM GitHubUser WHERE login = :login LIMIT 1")
    GitHubUser getUser(String login);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GitHubUser user);
}
