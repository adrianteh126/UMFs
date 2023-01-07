package com.example.umfs;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<Post> list;
    RecyclerView RVSearchResults;
    SearchView SVSearchResults;

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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Uploads");
        RVSearchResults = findViewById(R.id.RVSearchResults);
        SVSearchResults = findViewById(R.id.SVSearchResults);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //update the Deal data into the list
        if(databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        list = new ArrayList<Post>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            list.add(ds.getValue(Post.class));
                        }
                        SearchAdapter searchAdapter = new SearchAdapter(list);
                        RVSearchResults.setAdapter(searchAdapter);
                        RVSearchResults.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (SVSearchResults != null) {
            SVSearchResults.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    public void search(String queryText){
        ArrayList<Post> myList = new ArrayList<>();
        for(Post post : list) {
//            Log.d("Search_condition","deal.getDealDesc().toLowerCase()="+deal.getDealDesc().toLowerCase()
//                    +"\nqueryText.toLowerCase()="+queryText.toLowerCase()
//                    +"\n^ is true:"+deal.getDealDesc().toLowerCase().contains(queryText.toLowerCase()));
            if (post.getPostDescription().toLowerCase().contains(queryText.toLowerCase())
                || post.getPostTitle().toLowerCase().contains(queryText.toLowerCase())
                || post.getPostCategory().toLowerCase().contains(queryText.toLowerCase())
                || post.getPostBy().toLowerCase().contains(queryText.toLowerCase()) )
            {
                myList.add(post);
//                Log.d("Search_added","Added : "+deal);
            }
        }
//        Log.d("Search_result","List: "+myList.toString());
        SearchAdapter searchAdapter = new SearchAdapter(myList);
        RVSearchResults.setAdapter(searchAdapter);
        RVSearchResults.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

}