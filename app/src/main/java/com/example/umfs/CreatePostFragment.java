package com.example.umfs;


import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int PICK_IMAGE_REQUEST = 1;

    public ArrayList<Category> categoryArrayList;
    public ArrayAdapter<Category> arrayAdapter;

    private ImageView IVUserProfilePicture;
    private TextView TVUserID;
    private TextView TVCategory;
    private Spinner SpnrCategory;
    private EditText ETTitle;
    private EditText ETContent;
    private Button BtnPost;
    private Button BtnUploadImage;
    private ProgressBar PBUpload;
    private ImageView IVUpload;
    private TextView TVUserFaculty;


    private Uri UriImage;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private StorageTask uploadTask;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePostFragment newInstance(String param1, String param2) {
        CreatePostFragment fragment = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    //Spinner reference :
    //https://www.digitalocean.com/community/tutorials/android-spinner-drop-down-list
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TVUserID = view.findViewById(R.id.TVUserID);
        SpnrCategory = view.findViewById(R.id.SpnrCategory);
        BtnPost = view.findViewById(R.id.BtnPost);
        BtnUploadImage = view.findViewById(R.id.BtnUploadImage);
        PBUpload = view.findViewById(R.id.PBUpload);
        ETTitle = view.findViewById(R.id.ETTitle);
        ETContent = view.findViewById(R.id.ETContent);
        IVUpload = view.findViewById(R.id.IVUpload);
        TVUserFaculty = view.findViewById(R.id.TVUserFaculty);
        IVUserProfilePicture = view.findViewById(R.id.IVUserProfilePicture);

        storageReference = FirebaseStorage.getInstance().getReference("posts");
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        }
        else {
            Toast.makeText(getContext(), "User is not log in, unable to retrieve user data", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(),LoginActivity.class);
            startActivity(intent);
        }

        //fetch current user details
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equalsIgnoreCase(firebaseUser.getUid())) {
                        User user = new User(dataSnapshot.child("siswamail").getValue().toString()
                                ,dataSnapshot.child("username").getValue().toString()
                                ,dataSnapshot.child("password").getValue().toString()
                                ,dataSnapshot.child("Bio").getValue().toString()
                                ,dataSnapshot.child("Faculty").getValue().toString()
                                ,dataSnapshot.child("ProfilePicture").getValue().toString());
                        TVUserID.setText(user.getUsername());
                        TVUserFaculty.setText(user.getFaculty());
                        Picasso.get()
                                .load(user.getProfilePicture())
                                .placeholder(R.drawable.placeholder)
                                .into(IVUserProfilePicture);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BtnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
            private void openFileChooser() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        BtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();;
                }
                else {
                    uploadPost(UriImage);
                }
            }
        });

        SpnrCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //On selecting a spinner item
                String item = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(adapterView.getContext(), "Selected: "+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //fetch categories from database and update to arraylist
        categoryArrayList = new ArrayList<>();
        getCategories(); // get categories from database
        //Create adapter for spinner
        arrayAdapter = new ArrayAdapter<Category>(view.getContext(), android.R.layout.simple_spinner_item,categoryArrayList);
        //Drop down layout style - list view with radio button
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Attaching data adapter to spinner
        SpnrCategory.setAdapter(arrayAdapter);



    }

    public void getCategories() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Categories");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();
                for (DataSnapshot categorySnap : snapshot.getChildren()) {
                    String name = categorySnap.child("category").getValue().toString();
                    Category category = new Category(name);
                    categoryArrayList.add(category);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            UriImage = data.getData();
            Picasso.get().load(UriImage).into(IVUpload);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadPost(Uri UriImage) {
        if (UriImage != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(UriImage));
            uploadTask = fileReference.putFile(UriImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            PBUpload.setProgress(0);
                                        }
                                    },500);
                                    Toast.makeText(getContext(), "Upload successfully", Toast.LENGTH_SHORT).show();
                                    String postId = databaseReference.push().getKey();
                                    Post post = new Post(uri.toString()
                                            ,((Category)SpnrCategory.getSelectedItem()).getCategory()
                                            ,firebaseUser.getUid()
                                            ,ETTitle.getText().toString()
                                            ,ETContent.getText().toString()
                                            ,System.currentTimeMillis());
                                    databaseReference.child(postId).setValue(post);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            PBUpload.setProgress((int)progress);
                        }
                    });
        }
        else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}