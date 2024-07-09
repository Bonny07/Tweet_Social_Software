package com.baochenglin.pproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PostsDatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_POSTS = "posts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_CONTENT = "content";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_AUTHOR + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_CONTENT + " TEXT" + ")";
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addPost(String author, String time, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_CONTENT, content);
        db.insert(TABLE_POSTS, null, values);
        db.close();
    }

    public Cursor getAllPosts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_ID + " AS _id", COLUMN_AUTHOR, COLUMN_TIME, COLUMN_CONTENT };
        return db.query(TABLE_POSTS, columns, null, null, null, null, null);
    }



    public Cursor getPost(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_ID + " AS _id", COLUMN_AUTHOR, COLUMN_TIME, COLUMN_CONTENT };
        Cursor cursor = db.query(TABLE_POSTS,
                columns,
                COLUMN_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }


    public void updatePost(int id, String author, String time, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_CONTENT, content);
        db.update(TABLE_POSTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deletePost(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_POSTS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }



}

