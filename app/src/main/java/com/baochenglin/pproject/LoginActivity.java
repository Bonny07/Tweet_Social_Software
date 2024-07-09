package com.baochenglin.pproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextPhoneOrUsername, editTextPassword;
    private Button buttonLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        editTextPhoneOrUsername = findViewById(R.id.editTextPhoneOrUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String phoneOrUsername = editTextPhoneOrUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phoneOrUsername) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Both fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loginUser(phoneOrUsername, password)) {
            Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean loginUser(String phoneOrUsername, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseHelper.COLUMN_ID};
        String selection = "(username = ? OR phone = ?) AND password = ?";
        String[] selectionArgs = new String[] {phoneOrUsername, phoneOrUsername, password};

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean loggedIn = cursor.getCount() > 0;
        cursor.close();
        return loggedIn;
    }
}
