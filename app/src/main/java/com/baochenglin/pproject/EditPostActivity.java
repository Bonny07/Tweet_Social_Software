package com.baochenglin.pproject;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EditPostActivity extends AppCompatActivity {
    private EditText editTextAuthor, editTextTime, editTextContent;
    private Button buttonSave, buttonBack;
    private DatabaseHelper databaseHelper;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextTime = findViewById(R.id.editTextTime);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);
        buttonBack = findViewById(R.id.buttonBack);

        databaseHelper = new DatabaseHelper(this);
        postId = getIntent().getIntExtra("POST_ID", -1);

        loadPost();

        buttonSave.setOnClickListener(view -> updatePost());
        buttonBack.setOnClickListener(view -> finish());
    }

    private void loadPost() {
        Cursor cursor = databaseHelper.getPost(postId);
        if (cursor != null && cursor.moveToFirst()) {
            String author = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR));
            String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));
            String content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTENT));

            editTextAuthor.setText(author);
            editTextTime.setText(time);
            editTextContent.setText(content);
            cursor.close();
        }
    }

    private void updatePost() {
        String author = editTextAuthor.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        databaseHelper.updatePost(postId, author, time, content);
        finish();
    }
}
