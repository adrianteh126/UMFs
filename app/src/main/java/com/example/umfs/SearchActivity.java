package com.example.umfs;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    //Speech-to-text
    protected static final int RESULT_SPEECH = 1;
    ImageView IVTextToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Uploads");
        RVSearchResults = findViewById(R.id.RVSearchResults);
        SVSearchResults = findViewById(R.id.SVSearchResults);
        IVTextToSpeech = findViewById(R.id.IVTextToSpeech);

        IVTextToSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en_US");
                try {
                    startActivityForResult(intent,RESULT_SPEECH);
                } catch (Exception e) {
                    Toast.makeText(SearchActivity.this, "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    SVSearchResults.setQuery(text.get(0),true);
                }
                break;
        }
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