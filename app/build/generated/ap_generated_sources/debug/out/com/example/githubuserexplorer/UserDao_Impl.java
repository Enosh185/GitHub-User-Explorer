package com.example.githubuserexplorer;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GitHubUser> __insertionAdapterOfGitHubUser;

  public UserDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGitHubUser = new EntityInsertionAdapter<GitHubUser>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `GitHubUser` (`id`,`login`,`name`,`bio`,`avatarUrl`,`followers`,`following`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, GitHubUser value) {
        stmt.bindLong(1, value.id);
        if (value.login == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.login);
        }
        if (value.name == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.name);
        }
        if (value.bio == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.bio);
        }
        if (value.avatarUrl == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.avatarUrl);
        }
        stmt.bindLong(6, value.followers);
        stmt.bindLong(7, value.following);
      }
    };
  }

  @Override
  public void insert(final GitHubUser user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfGitHubUser.insert(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public GitHubUser getUser(final String login) {
    final String _sql = "SELECT * FROM GitHubUser WHERE login = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (login == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, login);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfLogin = CursorUtil.getColumnIndexOrThrow(_cursor, "login");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
      final int _cursorIndexOfAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarUrl");
      final int _cursorIndexOfFollowers = CursorUtil.getColumnIndexOrThrow(_cursor, "followers");
      final int _cursorIndexOfFollowing = CursorUtil.getColumnIndexOrThrow(_cursor, "following");
      final GitHubUser _result;
      if(_cursor.moveToFirst()) {
        _result = new GitHubUser();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfLogin)) {
          _result.login = null;
        } else {
          _result.login = _cursor.getString(_cursorIndexOfLogin);
        }
        if (_cursor.isNull(_cursorIndexOfName)) {
          _result.name = null;
        } else {
          _result.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfBio)) {
          _result.bio = null;
        } else {
          _result.bio = _cursor.getString(_cursorIndexOfBio);
        }
        if (_cursor.isNull(_cursorIndexOfAvatarUrl)) {
          _result.avatarUrl = null;
        } else {
          _result.avatarUrl = _cursor.getString(_cursorIndexOfAvatarUrl);
        }
        _result.followers = _cursor.getInt(_cursorIndexOfFollowers);
        _result.following = _cursor.getInt(_cursorIndexOfFollowing);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
