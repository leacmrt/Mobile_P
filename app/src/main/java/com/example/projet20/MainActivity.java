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


//Activité de lancement de l'application
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        BottomNavigationView navigation= findViewById(R.id.nav_view);
        //menu sous forme de Bottom navigation => en bas de l'écran

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;

                        switch (item.getItemId()) {
                            case R.id.navigation_home: //ouvre le fragment Home
                                fragment = new HomeFragment();
                                break;

                            case R.id.navigation_logout://quitte l'application
                                finish();
                                break;

                            case R.id.navigation_notifications://ouvre le fragment de settings
                                fragment = new NotificationsFragment();
                                break;
                        }

                        return loadFragment(fragment);//envoie à la fonction de changment de fragment courant
                    }


                });
        navigation.bringToFront();

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null)
        {
           getSupportFragmentManager()//à l'aide du Fragment manager
                    .beginTransaction()
                    .replace(R.id.container, fragment)//id container appartient au layout de l'activité principale
                    .commit(); //on place le fragment de notre choix en tant que fragment principal (ou container)
            return true;
        }
        return false;
    }


}