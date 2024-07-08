package com.baochenglin.pproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddPostActivity extends AppCompatActivity {
    private EditText editTextAuthor, editTextTime, editTextContent;
    private Button buttonSave, buttonBack;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // 绑定视图元素
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextTime = findViewById(R.id.editTextTime);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);
        buttonBack = findViewById(R.id.buttonBack);

        // 数据库助手初始化
        databaseHelper = new DatabaseHelper(this);

        // 配置按钮监听器
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        // 保存按钮事件处理
        buttonSave.setOnClickListener(view -> {
            savePost();
        });

        // 返回按钮事件处理
        buttonBack.setOnClickListener(view -> {
            finish(); // 关闭当前活动，返回到上一个活动
        });
    }

    private void savePost() {
        // 从输入字段获取数据
        String author = editTextAuthor.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        // 向数据库添加帖子
        databaseHelper.addPost(author, time, content);

        // 关闭活动并返回
        finish();
    }
}
