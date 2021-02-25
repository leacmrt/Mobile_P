package com.example.projet20;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.projet20.ui.dashboard.DashboardFragment;
import com.example.projet20.ui.home.HomeFragment;
import com.example.projet20.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        BottomNavigationView navigation= findViewById(R.id.nav_view);

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;

                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                fragment = new HomeFragment();
                                break;

                            case R.id.navigation_logout:
                                finish();
                                break;

                            case R.id.navigation_notifications:
                                fragment = new NotificationsFragment();
                                break;
                        }

                        return loadFragment(fragment);
                    }


                });
        navigation.bringToFront();

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
           getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


}