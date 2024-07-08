package com.baochenglin.pproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

public class EditFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private ListView listViewPosts;
    private SimpleCursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        dbHelper = new DatabaseHelper(getContext());
        listViewPosts = view.findViewById(R.id.listViewPosts);

        // Setup for the add button
        Button addButton = view.findViewById(R.id.buttonAddPost);
        addButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddPostActivity.class));
        });

        // Configuring the adapter for the list
        String[] from = new String[]{DatabaseHelper.COLUMN_AUTHOR, DatabaseHelper.COLUMN_TIME, DatabaseHelper.COLUMN_CONTENT};
        int[] to = new int[]{R.id.authorTextView, R.id.timeTextView, R.id.contentTextView};

        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.list_item_post, // The layout of each list item
                null, // Start with no cursor
                from,
                to,
                0);

        listViewPosts.setAdapter(adapter);
        updateListView();

        listViewPosts.setOnItemClickListener((parent, view1, position, id) -> {
            Cursor cursor = (Cursor) listViewPosts.getItemAtPosition(position);
            int indexId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID); // 获取 COLUMN_ID 的索引
            if (indexId != -1) { // 确保索引有效
                int postId = cursor.getInt(indexId);
                Intent intent = new Intent(getActivity(), EditPostActivity.class);
                intent.putExtra("POST_ID", postId);
                startActivity(intent);
            } else {
                // 可以处理错误或记录日志
                Log.e("EditFragment", "Invalid column index for ID.");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView() {
        Cursor cursor = dbHelper.getAllPosts();
        adapter.changeCursor(cursor);
    }
}
