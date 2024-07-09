package com.baochenglin.pproject;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private ListView listViewSearchResults;
    private EditText searchInput;
    private TextView emptyView;
    private SimpleCursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        dbHelper = new DatabaseHelper(getContext());
        listViewSearchResults = view.findViewById(R.id.listViewSearchResults);
        searchInput = view.findViewById(R.id.searchInput);
        emptyView = view.findViewById(R.id.emptyView);

        listViewSearchResults.setEmptyView(emptyView);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    emptyView.setText("Please enter content to search");
                    adapter.changeCursor(null);
                } else {
                    searchPosts(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setupListView();
        return view;
    }

    private void setupListView() {
        String[] from = new String[]{DatabaseHelper.COLUMN_AUTHOR, DatabaseHelper.COLUMN_TIME, DatabaseHelper.COLUMN_CONTENT};
        int[] to = new int[]{R.id.authorTextView, R.id.timeTextView, R.id.contentTextView};

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_post, null, from, to, 0);
        listViewSearchResults.setAdapter(adapter);
    }

    private void searchPosts(String query) {
        Cursor cursor = dbHelper.searchPosts(query);
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();

        if (cursor == null || cursor.getCount() == 0) {
            emptyView.setText("No related posts found");
        }
    }
}


