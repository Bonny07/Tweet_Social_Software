package com.baochenglin.pproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;
    private DatabaseHelper dbHelper;
    private String currentUsername;

    public PostAdapter(List<Post> posts, DatabaseHelper dbHelper, String currentUsername) {
        this.posts = posts;
        this.dbHelper = dbHelper;
        this.currentUsername = currentUsername;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.authorTextView.setText(post.getAuthor());
        holder.timeTextView.setText(post.getTime());
        holder.contentTextView.setText(post.getContent());

        holder.favoriteButton.setOnClickListener(view -> {
            boolean isFavoriteAdded = dbHelper.addFavorite(currentUsername, post.getId());
            Log.d("PostAdapter", "Attempting to add favorite: User = " + currentUsername + ", Post ID = " + post.getId() + ", Success = " + isFavoriteAdded);
            if (isFavoriteAdded) {
                Toast.makeText(holder.itemView.getContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(holder.itemView.getContext(), "Failed to add to favorites!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView authorTextView;
        TextView timeTextView;
        TextView contentTextView;
        Button favoriteButton;

        PostViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }
    }
}
