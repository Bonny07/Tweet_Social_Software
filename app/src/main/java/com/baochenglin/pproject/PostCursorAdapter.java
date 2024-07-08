package com.baochenglin.pproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PostCursorAdapter extends CursorAdapter {
    private int authorIndex;
    private int timeIndex;
    private int contentIndex;

    public PostCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        cacheColumnIndices(cursor);
    }

    private void cacheColumnIndices(Cursor cursor) {
        if (cursor != null) {
            authorIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR);
            timeIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIME);
            contentIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_post, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView authorView = view.findViewById(R.id.authorTextView);
        TextView timeView = view.findViewById(R.id.timeTextView);
        TextView contentView = view.findViewById(R.id.contentTextView);

        // 使用预先获取的列索引
        String author = cursor.getString(authorIndex);
        String time = cursor.getString(timeIndex);
        String content = cursor.getString(contentIndex);

        authorView.setText(author);
        timeView.setText(time);
        contentView.setText(content);
    }
}
