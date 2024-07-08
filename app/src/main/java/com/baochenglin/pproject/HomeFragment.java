package com.baochenglin.pproject;

import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private DatabaseHelper databaseHelper;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        postAdapter = new PostAdapter(new ArrayList<>()); // 假设适配器需要一个列表
        recyclerView.setAdapter(postAdapter);

        loadPosts();

        return view;
    }

    private void loadPosts() {
        Cursor cursor = databaseHelper.getAllPosts();
        ArrayList<Post> posts = new ArrayList<>(); // 假设有一个 Post 类来处理数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String author = cursor.getString(cursor.getColumnIndex("author"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            posts.add(new Post(id, author, time, content)); // 添加到列表
        }
        cursor.close();
        postAdapter.setPosts(posts); // 更新数据
        postAdapter.notifyDataSetChanged(); // 刷新适配器
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts(); // 每次返回此片段时刷新数据
    }

    public void refreshData() {
        // 此处添加获取最新数据并更新适配器的代码
        if (postAdapter != null) {
            ArrayList<Post> newPosts = fetchDataFromDatabase();  // 假设您有一个方法来获取数据
            postAdapter.setPosts(newPosts);
            postAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<Post> fetchDataFromDatabase() {
        // 这里应实现从数据库获取数据的逻辑
        return new ArrayList<>();  // 返回新的数据列表
    }
}
