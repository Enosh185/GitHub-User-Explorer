package com.example.githubuserexplorer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context context;
    private final List<GitHubUser> originalList;
    private List<GitHubUser> filteredList;

    public UserAdapter(List<GitHubUser> users, Context context) {
        this.context = context;
        this.originalList = new ArrayList<>(users);
        this.filteredList = new ArrayList<>(users);
    }

    public void addMoreUsers(List<GitHubUser> moreUsers) {
        originalList.addAll(moreUsers);
        filter(""); // Refresh view to include new users
    }

    public void clearUsers() {
        originalList.clear();
        filteredList.clear();
        notifyDataSetChanged();
    }

    public void filter(String query) {
        if (query == null || query.trim().isEmpty()) {
            filteredList = new ArrayList<>(originalList);
        } else {
            String lowerQuery = query.toLowerCase();
            List<GitHubUser> result = new ArrayList<>();
            for (GitHubUser user : originalList) {
                if (user.getLogin().toLowerCase().contains(lowerQuery) ||
                        (user.getName() != null && user.getName().toLowerCase().contains(lowerQuery))) {
                    result.add(user);
                }
            }
            filteredList = result;
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GitHubUser user = filteredList.get(position);
        holder.username.setText(user.getLogin());
        holder.name.setText(user.getName() != null ? user.getName() : "");

        Glide.with(context)
                .load(user.getAvatarUrl())
                .circleCrop()
                .into(holder.avatar);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("username", user.getLogin());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView username, name;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.user_avatar);
            username = itemView.findViewById(R.id.user_username);
            name = itemView.findViewById(R.id.user_name);
        }
    }
}
