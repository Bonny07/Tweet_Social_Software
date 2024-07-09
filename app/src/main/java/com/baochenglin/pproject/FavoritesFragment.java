package com.baochenglin.pproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritesFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private String currentUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dbHelper = new DatabaseHelper(getContext());

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("loggedInUsername", "defaultUsername");

        loadFavorites();
        return view;
    }

    private void loadFavorites() {
        Cursor cursor = dbHelper.getFavoritesByUsername(currentUsername);  // 使用从 SharedPreferences 获取的用户名
        FavoritesAdapter adapter = new FavoritesAdapter(cursor, new FavoritesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postId) {
                if (dbHelper.removeFavorite(currentUsername, postId)) {
                    Toast.makeText(getContext(), "Removed from favorites!", Toast.LENGTH_SHORT).show();
                    loadFavorites();  // Reload favorites to update the list
                } else {
                    Toast.makeText(getContext(), "Failed to remove from favorites!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    private static class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
        private Cursor cursor;
        private OnItemClickListener listener;

        public FavoritesAdapter(Cursor cursor, OnItemClickListener listener) {
            this.cursor = cursor;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (cursor.moveToPosition(position)) {
                String content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT));
                holder.textViewContent.setText(content);
                holder.buttonRemove.setOnClickListener(view -> listener.onItemClick(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))));
            }
        }

        @Override
        public int getItemCount() {
            return cursor != null ? cursor.getCount() : 0;
        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
            if (cursor != null) {
                cursor.close(); // 关闭Cursor
            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewContent;
            public Button buttonRemove;

            public ViewHolder(View itemView) {
                super(itemView);
                textViewContent = itemView.findViewById(R.id.textViewContent);
                buttonRemove = itemView.findViewById(R.id.buttonRemove);
            }
        }

        public interface OnItemClickListener {
            void onItemClick(int postId);
        }
    }
}
