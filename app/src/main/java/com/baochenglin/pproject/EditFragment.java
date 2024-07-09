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
    private DatabaseHelper dbHelper;  // 声明为成员变量
    private ListView listViewPosts;
    private SimpleCursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        dbHelper = new DatabaseHelper(getContext());  // 初始化数据库帮助类
        listViewPosts = view.findViewById(R.id.listViewPosts);

        // 设置添加帖子按钮的点击事件
        Button addButton = view.findViewById(R.id.buttonAddPost);
        addButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddPostActivity.class));
        });

        // 配置 SimpleCursorAdapter
        String[] from = new String[]{DatabaseHelper.COLUMN_AUTHOR, DatabaseHelper.COLUMN_TIME, DatabaseHelper.COLUMN_CONTENT};
        int[] to = new int[]{R.id.authorTextView, R.id.timeTextView, R.id.contentTextView};

        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.list_item_editable,  // 使用可编辑的列表项布局
                null,
                from,
                to,
                0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = super.newView(context, cursor, parent);
                configureButtons(view, cursor);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                super.bindView(view, context, cursor);
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

    private void configureButtons(View view, Cursor cursor) {
        Button editButton = view.findViewById(R.id.buttonEdit);
        Button deleteButton = view.findViewById(R.id.buttonDelete);

        editButton.setOnClickListener(v -> {
            int postId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            Intent intent = new Intent(getActivity(), EditPostActivity.class);
            intent.putExtra("POST_ID", postId);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            int postId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            dbHelper.deletePost(postId);
            updateListView();
        });
    }
}
