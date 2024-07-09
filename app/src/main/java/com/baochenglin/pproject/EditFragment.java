package com.baochenglin.pproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

        Button addButton = view.findViewById(R.id.buttonAddPost);
        addButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddPostActivity.class));
        });

        String[] from = new String[]{DatabaseHelper.COLUMN_AUTHOR, DatabaseHelper.COLUMN_TIME, DatabaseHelper.COLUMN_CONTENT};
        int[] to = new int[]{R.id.authorTextView, R.id.timeTextView, R.id.contentTextView};

        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.list_item_editable,
                null,
                from,
                to,
                0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = super.newView(context, cursor, parent);
                int postId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                configureButtons(view, postId);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                super.bindView(view, context, cursor);
                int postId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                configureButtons(view, postId);
            }
        };

        listViewPosts.setAdapter(adapter);
        updateListView();

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
        adapter.notifyDataSetChanged();
    }

    private void configureButtons(View view, int postId) {
        Button editButton = view.findViewById(R.id.buttonEdit);
        Button deleteButton = view.findViewById(R.id.buttonDelete);

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditPostActivity.class);
            intent.putExtra("POST_ID", postId);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            dbHelper.deletePost(postId);
            updateListView();
        });
    }
}
