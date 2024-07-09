package com.baochenglin.pproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private Fragment searchFragment;
    private FavoritesFragment favoriteFragment;  // 应使用 FavoritesFragment 类型，假设它是 Fragment 的子类
    private Fragment editFragment;
    private Fragment currentFragment;
    private ImageView imageViewSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        favoriteFragment = new FavoritesFragment();  // 确保已正确实例化
        editFragment = new EditFragment();
        currentFragment = homeFragment;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    selectedFragment = homeFragment;
                } else if (id == R.id.navigation_search) {
                    selectedFragment = searchFragment;
                } else if (id == R.id.navigation_favorites) {
                    selectedFragment = favoriteFragment;
                } else if (id == R.id.navigation_edit) {
                    selectedFragment = editFragment;
                }

                if (selectedFragment != null && currentFragment != selectedFragment) {
                    currentFragment = selectedFragment;
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });

        // Set the default selection
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        }

        imageViewSettings = findViewById(R.id.imageViewSettings);
        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (homeFragment.isVisible()) {
            homeFragment.refreshData();
        }
    }
}
