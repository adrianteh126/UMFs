package com.example.umfs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup action bar
        Toolbar toolbar = findViewById(R.id.TBMainAct);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.DLMain);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this
                , drawerLayout,toolbar
                ,R.string.navigation_drawer_open
                ,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // setup navigation graph (menu)
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFMain);
        NavController navController = host.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        setupNavMenu(navController);
        setupBottomNavMenu(navController);
    }

    //setupBottomNavMenu(navController) method
    private void setupNavMenu (NavController navController) {
//        NavigationView sideNav = findViewById(R.id.sideNav);
//        NavigationUI.setupWithNavController(sideNav, navController);
    }

    private void setupBottomNavMenu (NavController navController) {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_side,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            Navigation.findNavController(this,R.id.NHFMain).navigate(item.getItemId());
            return true;
        }
        catch (Exception ex) {
            return super.onOptionsItemSelected(item);
        }
    }

}