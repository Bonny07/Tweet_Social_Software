package com.baochenglin.pproject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPhone, editTextPassword;
    private Button buttonSave, buttonLogout, buttonBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbHelper = new DatabaseHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSave = findViewById(R.id.buttonSave);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonBack = findViewById(R.id.buttonBack);

        buttonSave.setOnClickListener(v -> saveChanges());
        buttonLogout.setOnClickListener(v -> logout());
        buttonBack.setOnClickListener(v -> onBackPressed());

        loadUserData();
    }

    private void loadUserData() {
        String currentUsername = getCurrentUsername();
        if (currentUsername == null || currentUsername.isEmpty()) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_PHONE, DatabaseHelper.COLUMN_PASSWORD},
                DatabaseHelper.COLUMN_USERNAME + "=?", new String[]{currentUsername}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME));
            String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE));
            String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));

            editTextUsername.setText(username);
            editTextPhone.setText(phone);
            editTextPassword.setText(password);
            cursor.close();
        } else {
            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_LONG).show();
        }
        db.close();
    }


    private String getCurrentUsername() {
        // 从 SharedPreferences 获取当前用户名
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getString("loggedInUsername", ""); // 使用与 LoginActivity 中相同的键
    }



    private void saveChanges() {
        String username = editTextUsername.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PHONE, phone);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        int updateCount = db.update(DatabaseHelper.TABLE_USERS, values,
                DatabaseHelper.COLUMN_USERNAME + "=?", new String[]{username});

        if (updateCount > 0) {
            Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save settings", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private void logout() {
        // Clear the user session details from SharedPreferences
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
