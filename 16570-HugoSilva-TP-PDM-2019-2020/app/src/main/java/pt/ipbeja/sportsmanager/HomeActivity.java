package pt.ipbeja.sportsmanager;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigation = findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(navlistener);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.frg_space,
                    new Product()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_products:
                    menuItem.setChecked(true);
                    selectedFragment = new Product();
                    break;
                case R.id.nav_order:
                    menuItem.setChecked(true);
                    // TODO Change fragment object
                    selectedFragment = new Fragment();
                    break;
                case R.id.nav_bills:
                    menuItem.setChecked(true);
                    selectedFragment = new Bills();
                    break;
            }

            getFragmentManager().beginTransaction().replace(R.id.frg_space,
                    selectedFragment).commit();

            return false;
        }
    };


}