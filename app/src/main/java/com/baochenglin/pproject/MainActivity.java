package com.baochenglin.pproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment searchFragment = new SearchFragment();
    private final Fragment notificationsFragment = new NotificationsFragment();
    private final Fragment editFragment = new EditFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
