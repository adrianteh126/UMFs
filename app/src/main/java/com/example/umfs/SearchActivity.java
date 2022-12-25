package com.example.umfs;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        //setup action bar (disabled - should not have action bar)
//        Toolbar toolbar = findViewById(R.id.TBMainAct);
//        setSupportActionBar(toolbar);
//        DrawerLayout drawerLayout = findViewById(R.id.DLMain);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this
//                , drawerLayout,toolbar
//                ,R.string.navigation_drawer_open
//                ,R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_search,menu);
//        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
////        The Query Hint can be change later
//        searchView.setQueryHint("Trending topic : World Cup 2022");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            TODO: Implement the search function here
//// https://www.youtube.com/watch?v=M3UDh9mwBd8&ab_channel=Foxandroid
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                SearchLandingFragment landingFragment = null;
//                landingFragment.getArrayAdapter().getFilter().filter(newText);
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }
}