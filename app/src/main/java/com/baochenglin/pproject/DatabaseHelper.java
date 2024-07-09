package com.baochenglin.pproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PostsDatabase";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_POSTS = "posts";
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FAVORITES = "favorites";

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
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_FAVORITES + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean addUser(String username, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_FAVORITES, "");  // 初始化时无收藏

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;  // 如果用户添加成功，返回 true
    }

    // 更新用户收藏
    public boolean updateUserFavorites(String username, String favorites) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITES, favorites);

        int result = db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});
        db.close();
        return result > 0;
    }

    // 通过用户名获取用户信息
    public Cursor getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
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

    public Cursor searchPosts(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID + " AS _id", COLUMN_AUTHOR, COLUMN_TIME, COLUMN_CONTENT};
        String selection = COLUMN_AUTHOR + " LIKE ? OR " + COLUMN_CONTENT + " LIKE ?";
        String[] selectionArgs = new String[] {"%" + query + "%", "%" + query + "%"};

        return db.query(TABLE_POSTS, columns, selection, selectionArgs, null, null, null);
    }

    // 添加收藏
    public boolean addFavorite(String username, int postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, new String[]{COLUMN_FAVORITES},
                    COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);

            String currentFavorites;
            if (cursor != null && cursor.moveToFirst()) {
                currentFavorites = cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITES));
            } else {
                Log.d("DatabaseHelper", "User not found or no favorites column value.");
                return false;  // 用户不存在或收藏列为空
            }

            String updatedFavorites = currentFavorites == null || currentFavorites.isEmpty() ? String.valueOf(postId) :
                    currentFavorites + "," + postId;
            ContentValues values = new ContentValues();
            values.put(COLUMN_FAVORITES, updatedFavorites);

            int rowsUpdated = db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
            return rowsUpdated > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }




    // 移除收藏
    public boolean removeFavorite(String username, int postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, new String[]{COLUMN_FAVORITES},
                    COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String currentFavorites = cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITES));
                String[] favoritesArray = currentFavorites.split(",");
                StringBuilder updatedFavorites = new StringBuilder();
                for (String id : favoritesArray) {
                    if (!id.equals(String.valueOf(postId))) {
                        if (updatedFavorites.length() > 0) updatedFavorites.append(",");
                        updatedFavorites.append(id);
                    }
                }
                ContentValues values = new ContentValues();
                values.put(COLUMN_FAVORITES, updatedFavorites.toString());
                db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
                return true;
            }
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public Cursor getFavoritesByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String ids = getUserFavorites(username);
            if (!ids.isEmpty()) {
                cursor = db.query(TABLE_POSTS, null, COLUMN_ID + " IN (" + ids + ")", null, null, null, null);
            } else {
                // 创建一个空的MatrixCursor，避免返回null
                String[] columns = {COLUMN_ID, COLUMN_AUTHOR, COLUMN_TIME, COLUMN_CONTENT};
                cursor = new MatrixCursor(columns);
            }
            return cursor;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching favorites", e);
            return null; // 如果发生异常，返回null
        } finally {
            // 注意不要在这里关闭数据库连接
            // db.close();  // 不在这里关闭数据库连接
        }
    }

    private String getUserFavorites(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, new String[]{COLUMN_FAVORITES},
                    COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITES));
            }
            return ""; // Return empty string if no favorites
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // db.close(); // 不在这里关闭数据库连接
        }
    }




}

