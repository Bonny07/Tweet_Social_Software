package com.baochenglin.pproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private HomeFragment homeFragment; // Changed to specific type to handle refresh directly
    private Fragment searchFragment;
    private Fragment notificationsFragment;
    private Fragment editFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the fragments here to avoid issues with getActivity() in fragments
        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        notificationsFragment = new NotificationsFragment();
        editFragment = new EditFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    selectedFragment = homeFragment;
                    homeFragment.refreshData();
                } else if (id == R.id.navigation_search) {
                    selectedFragment = searchFragment;
                } else if (id == R.id.navigation_notifications) {
                    selectedFragment = notificationsFragment;
                } else if (id == R.id.navigation_edit) {
                    selectedFragment = editFragment;
                }

                if (selectedFragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (homeFragment.isVisible()) {
            homeFragment.refreshData();
        }
    }
}
