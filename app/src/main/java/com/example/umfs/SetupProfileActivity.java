package com.example.umfs;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText ETBioSetup;
    private ImageView IVProfilePicSetup;
    private Button BTCreateProfile;
    private Spinner SpinnerFaculty;
    private String item;

    Uri imageUri;   //for image

    StorageReference storageReference;  //Cloud Storage reference
    FirebaseUser currentUser;   //store the current FirebaseUser authenticated
    FirebaseDatabase databaseRoot;  //access to UMFs realtime database @ firebase
    DatabaseReference databaseReference;    //reference to node under UMFs database aka Users node
    DatabaseReference userRef;  //reference to specific user under "Users" node

    ActivityResultLauncher<Intent> activityResultLauncher; //to launch open files activity
    String currentUserID;

    String[] StrFacList = {"Academy of Islamic Studies",
            "Academy of Malay Studies" ,
            "Centre for Foundation Studies" ,
            "Faculty of Arts and Social Sciences" ,
            "Faculty of Built Environment" ,
            "Faculty of Business and Economics" ,
            "Faculty of Computer Science and Information Technology (FCSIT)" ,
            "Faculty of Creative Arts" ,
            "Faculty of Dentistry" ,
            "Faculty of Education" ,
            "Faculty of Engineering" ,
            "Faculty of Language and Linguistics" ,
            "Faculty of Law" ,
            "Faculty of Medicine" ,
            "Faculty of Pharmacy" ,
            "Faculty of Science" ,
            "Faculty of Sports and Exercise Science"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        ETBioSetup = findViewById(R.id.ETBioSetup);
        IVProfilePicSetup = findViewById(R.id.IVProfilePicSetup);
        BTCreateProfile = findViewById(R.id.BTCreateProfile);
        SpinnerFaculty = findViewById(R.id.SpinnerFaculty);

        try {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            currentUserID = currentUser.getUid();
        } catch (Exception e){
            Toast.makeText(SetupProfileActivity.this, "Error in getting user", Toast.LENGTH_SHORT).show();
        }

        try{
            //TODO : Cloud Storage reference, to store profile pictures of user
            storageReference = FirebaseStorage.getInstance().getReference("ProfilePicture");
        } catch (Exception e){
            Toast.makeText(SetupProfileActivity.this, "Error in Cloud Storage", Toast.LENGTH_SHORT).show();
        }

        try{
            //get access to UMFs realtime database
            String databaseURL = "https://umfs-2cb55-default-rtdb.asia-southeast1.firebasedatabase.app";
            databaseRoot = FirebaseDatabase.getInstance(databaseURL);
            //reference to Users node under UMFs realtime database
            databaseReference = databaseRoot.getReference().child("Users");
            //reference to specific user file under "Users" node
            userRef = databaseReference.child(currentUserID);
        } catch (Exception e){
            Toast.makeText(SetupProfileActivity.this, "Error in Realtime Database", Toast.LENGTH_SHORT).show();
        }

        SpinnerFaculty.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> FacList = new ArrayList<String>();
        for(int n=0;n<StrFacList.length;n++){
            FacList.add(StrFacList[n]);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FacList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        SpinnerFaculty.setAdapter(dataAdapter);

        IVProfilePicSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        //To launch file chooser activity
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            imageUri = result.getData().getData();
                            IVProfilePicSetup.setImageURI(imageUri);
                        }
                    }
                });

        BTCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri!=null){
                    uploadProfilePicture();
                }
                else{
                    uploadNullProfilePicture();
                }
                uploadProfile();
            }
        });

    }

    private void uploadNullProfilePicture() {
        //TODO : If No Profile Picture, will upload @drawable/man as profile picture instead
        // but the imageView must be converted to Bytes first
        IVProfilePicSetup = findViewById(R.id.IVProfilePicSetup);
        IVProfilePicSetup.setDrawingCacheEnabled(true);
        IVProfilePicSetup.buildDrawingCache();
        Bitmap bitmap = IVProfilePicSetup.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference fileReference = storageReference.child(currentUserID).child(System.currentTimeMillis() + ".JPEG");
        UploadTask uploadTask1 = fileReference.putBytes(data);
        uploadTask1.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(SetupProfileActivity.this, "Failure to upload Null Profile Picture", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                Map<String, Object> picUpdates = new HashMap<>();
                                picUpdates.put("ProfilePicture", imageUrl);
                                userRef.updateChildren(picUpdates);

                                Toast.makeText(SetupProfileActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
    }


    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadProfilePicture() {
        StorageReference fileReference = storageReference.child(currentUserID).child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        Map<String, Object> picUpdates = new HashMap<>();
                                        picUpdates.put("ProfilePicture", imageUrl);
                                        userRef.updateChildren(picUpdates);

                                        Toast.makeText(SetupProfileActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetupProfileActivity.this, "Failure to upload Profile Picture", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadProfile() {
        try {
            // TODO : Upload User Profile Data in Realtime Database under the specific user node
            Map<String, Object> profileUpdates = new HashMap<>();
            profileUpdates.put("Faculty", item);
            profileUpdates.put("Bio", ETBioSetup.getText().toString().trim());
            userRef.updateChildren(profileUpdates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(SetupProfileActivity.this, "Profile Successfully Set-Up", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SetupProfileActivity.this, MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SetupProfileActivity.this, "Failure to upload Profile Data", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e){
            Toast.makeText(SetupProfileActivity.this, "Failed to Upload Profile Data", Toast.LENGTH_LONG).show();
        }
    }
    private void chooseFile() {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction((Intent.ACTION_GET_CONTENT));
            activityResultLauncher.launch(intent);

        } catch(Exception e) {
            Toast.makeText(SetupProfileActivity.this, "Choosing Files Failed. Try Again", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        // On selecting a spinner item
        item = adapterView.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}